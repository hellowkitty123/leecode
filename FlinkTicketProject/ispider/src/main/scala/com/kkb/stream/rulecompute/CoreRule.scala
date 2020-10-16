package com.kkb.stream.rulecompute

import java.util

import com.kkb.stream.common.bean.{AntiCalculateResult, FlowCollocation, FlowScoreResult, ProcessedData}
import com.kkb.stream.common.util.jedis.PropertiesUtil
import com.kkb.stream.dataprocess.businessProcess.BusinessProcess
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.flink.api.scala._
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisClusterConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}


import scala.collection.mutable.ArrayBuffer

/**
  * 所有指标计算的工具类
  */
object CoreRule {

  /**
    *
    * 所有指标计算
    *
    * @param dataStream
    * @return
    */
  def processTarget(dataStream: DataStream[(ProcessedData, util.HashMap[String, Any])]) = {

    //获取消息中的remoteAddr
    val result: DataStream[AntiCalculateResult] = dataStream.map(x => {

      //获取ProcessedData对象
      val processedData = x._1

      //获取客户端ip
      val ip: String = processedData.remoteAddr

      //获取访问页面
      val request: String = processedData.request

      //从数据库中获取关键页面信息
      val criticalPagesArray: ArrayBuffer[String] = x._2.get("criticalPages").asInstanceOf[ArrayBuffer[String]]

      //判断页面是否为关键页面
      var flag = false
      for (page <- criticalPagesArray if !flag) {
        if (request.matches(page)) {
          flag = true
        }
      }

      //获取数据库流程列表
      val flowCollocations: ArrayBuffer[FlowCollocation] = x._2.get("flowRule").asInstanceOf[ArrayBuffer[FlowCollocation]]

      //封装结果返回
      (ip, processedData, flag, flowCollocations)

    }).keyBy(0)
      .timeWindow(Time.seconds(10))
      .process(new MyTargetProcessWindowFunction)

    result.print()
    //返回
    result


  }


  /**
    * 构建redis sink
    */
  def getRedisSink():RedisSink[Tuple2[String,FlowScoreResult]]={
    //获取FlinkJedisClusterConfig配置对象
    val jedisClusterConfig: FlinkJedisClusterConfig = BusinessProcess.getFlinkJedisClusterConfig()

    //创建redis  sink
    val redisSink = new RedisSink[Tuple2[String,FlowScoreResult]](jedisClusterConfig,new IpBlackRedisMapper)

    redisSink
  }

}


  //自定义ProcessWindowFunction,统计指标
  class MyTargetProcessWindowFunction extends ProcessWindowFunction[(String, ProcessedData, Boolean, ArrayBuffer[FlowCollocation]), AntiCalculateResult, Tuple, TimeWindow] {

    override def process(key: Tuple, context: Context, elements: Iterable[(String, ProcessedData, Boolean, ArrayBuffer[FlowCollocation])], out: Collector[AntiCalculateResult]): Unit = {
      val elementList: List[(String, ProcessedData, Boolean, ArrayBuffer[FlowCollocation])] = elements.toList
      //获取流程列表
      val flowCollocations: Array[FlowCollocation] = elementList(0)._4.toArray

      //获取分组key
      val ip: String = key.getField[String](0)

      //todo:1、单位时间内的IP的总访问量
      val ipAccessCounts = elementList.size

      //todo:2、单位时间内的IP关键页面访问总量
      val criticalPagesList: List[ProcessedData] = elementList.filter(x => x._3).map(_._2)
      val criticalPageAccessCounts: Int = criticalPagesList.size

      //todo:3、单位时间内的IP访问UA种类数统计
      val userAgentCounts: Int = elementList.map(x => x._2.httpUserAgent).distinct.size

      //todo:4、单位时间内的IP关键页面最短访问间隔
      val timeList: util.List[Long] = RuleUtils.calculateIntervals(criticalPagesList)
      //计算最短访问间隔
      val critivalPageMinInterval: Int = RuleUtils.getMinInterval(timeList).toInt

      //todo:5、单位时间内的IP小于最短访问间隔（自设）的关键页面查询次数
      //计算访问间隔小于给定值的次数
      val accessPageIntervalLessThanDefault: Int = RuleUtils.intervalsLessThanDefault(timeList)


      //todo:6、单位时间内的IP访问不同行程的次数
      val differentTripQuerysCounts: Int = elementList.map(x => (x._2.requestParams.depcity, x._2.requestParams.arrcity)).distinct.size

      //todo:7、单位时间内的IP访问关键页面不同的cookie数
      val criticalCookies: Int = criticalPagesList.map(x => x.cookieValue_JSESSIONID).distinct.size


      //todo:8、这条记录对应的所有标签封装到map中
      val paramMap = scala.collection.mutable.Map[String, Int]()
        paramMap += ("ip" -> ipAccessCounts)
        paramMap += ("criticalPages" -> criticalPageAccessCounts)
        paramMap += ("userAgent" -> userAgentCounts)
        paramMap += ("criticalPagesAccTime" -> critivalPageMinInterval)
        paramMap += ("criticalPagesLessThanDefault" -> accessPageIntervalLessThanDefault)
        paramMap += ("flightQuery" -> differentTripQuerysCounts)
        paramMap += ("criticalCookies" -> criticalCookies)

      // println(paramMap.toString())

      /**
        * 计算打分结果
        * paramMap：在5分钟之内统计的结果
        * FlowCollocations：数据库规则，规定5分钟内不允许超过限制的值
        * 最终结果为：Array[（流程Id，流程得分，流程阈值,是否大于阈值大于阈值定义为爬虫）]
        */
      val flowsScore: Array[FlowScoreResult] = RuleUtils.calculateRuleScore(paramMap, flowCollocations)


      //todo:封装结果返回
      val antiCalculateResult = AntiCalculateResult(ip,
                                                    ipAccessCounts,
                                                    criticalPageAccessCounts,
                                                    userAgentCounts,
                                                    critivalPageMinInterval,
                                                    accessPageIntervalLessThanDefault,
                                                    differentTripQuerysCounts,
                                                    criticalCookies,
                                                    flowsScore)

      out.collect(antiCalculateResult)
    }
}


/**
  * 保存黑名单到redis的sink
  */
class IpBlackRedisMapper  extends RedisMapper[Tuple2[String,FlowScoreResult]]{

  override def getCommandDescription: RedisCommandDescription = {
    //获取redis key的过期时间 默认是1小时
    val ttl: Int = PropertiesUtil.getStringByKey("cluster.exptime.anti_black_list","jedisConfig.properties").toInt

    //设置插入数据到redis的命令，带上key的过期时间
    new RedisCommandDescription(RedisCommand.SET)

  }

  //指定key
  override def getKeyFromData(data: (String, FlowScoreResult)): String = {
     val keyPrefix: String = PropertiesUtil.getStringByKey("cluster.key.anti_black_list","jedisConfig.properties")
     val ip: String = data._1
     val flowId: String = data._2.flowId
     //拼接key     key前缀:ip:flowId
     val key =s"${keyPrefix}:${ip}:${flowId}"
     key
  }

  //指定value
  override def getValueFromData(data: (String, FlowScoreResult)): String = {
    val flowScoreResult: FlowScoreResult = data._2
    //拼接value   flowScore|flowStrategyCode|hitRules
     val value= s"${flowScoreResult.flowScore}|${flowScoreResult.flowStrategyCode}|${flowScoreResult.hitRules.mkString(",")}"
     value
  }

}


