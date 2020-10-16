package com.kkb.stream.rulecompute

import java.util
import java.util.Date

import com.kkb.stream.common.bean.{FlowCollocation, FlowScoreResult, ProcessedData}
import org.apache.commons.lang3.time.FastDateFormat

import scala.collection.mutable.{ArrayBuffer, ListBuffer}


/**
  * 规则处理的工具类
  */
object RuleUtils {


  /**
    * 计算关键页面访问时间间隔，单位毫秒
    *
    * @param accTimes 时间Iterable
    * @return list 集合
    */
  def calculateIntervals(accTimes: Iterable[(String, String)]): java.util.List[Long] = {
    val timeList: java.util.List[Long] = new java.util.ArrayList[Long]
    val instance: FastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")

    //格式化时间
    for (time <- accTimes) {

      val accessTime: String = time._2
          if (!"0".equalsIgnoreCase(accessTime)) {
        try {
          val timeStr: String = accessTime.substring(0, accessTime.indexOf("+")).replace("T", " ")
          timeList.add(instance.parse(timeStr).getTime)
        } catch {
          case e: Exception =>
            e.printStackTrace()
        }
      }
    }
    timeList
  }

  /**
    * 计算关键页面访问时间间隔，单位毫秒
    *
    * @param accTimes 时间Iterable
    * @return list 集合
    */
  def calculateIntervals(accTimes: List[ProcessedData]): java.util.List[Long] = {
    val timeList: java.util.List[Long] = new java.util.ArrayList[Long]
    //格式化时间
    val instance: FastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")

    for (time <- accTimes) {

      val accessTime: String = time.timeIso8601
      if (!"0".equalsIgnoreCase(accessTime)) {
        try {
          val timeStr: String = accessTime.substring(0, accessTime.indexOf("+")).replace("T", " ")
          timeList.add(instance.parse(timeStr).getTime)
        } catch {
          case e: Exception =>
            e.printStackTrace()
        }
      }
    }
    timeList
  }



  /**
    *
    * 计算时间间隔列表
    * @param timeList 集合
    * @return
    */
  def computeInterval(timeList: java.util.List[Long]): Array[AnyRef]= {
     val array =  timeList.toArray
    //排序
    util.Arrays.sort(array)

    //封装时间间隔的list
    val intervalList = new util.ArrayList[Long]()

    if (array.length >= 2) {
      for (i <- 1 until array.length) {
         val time1=array(i-1).asInstanceOf[Long]
         val time2=array(i).asInstanceOf[Long]
         val interval =time2-time1
        intervalList.add(interval)
      }
    }
    val arrayInterval = intervalList.toArray

    //排序
    util.Arrays.sort(arrayInterval)

    //返回排序的结果
    arrayInterval
  }


  /**
    * 计算最短访问间隔
    * @param timeList
    * @return
    */
  def getMinInterval(timeList: java.util.List[Long]):Long={
        val timeInterval: Array[AnyRef] = computeInterval(timeList)
         //取出排序之后的第一个元素
        timeInterval(0).asInstanceOf[Long]/1000
  }


  /**
    *小于最短访问间隔（自设）的关键页面查询次数
    * @param timeList 集合
    * @return 访问次数
    *
    */
  def intervalsLessThanDefault(timeList: java.util.List[Long]): Int = {
    val timeInterval: Array[AnyRef] = computeInterval(timeList)

    //单位时间内最短访问间隔（自设） 10秒
    val defaultMinInterval:Long = 10
    var accessCount = 0
    if (timeInterval != null && timeInterval.length > 0) {
      for (i <- 0 until timeInterval.length) {

        if (timeInterval(i).asInstanceOf[Long]/1000 < defaultMinInterval) {
          accessCount = accessCount + 1
        }
      }
    }
    accessCount
  }



  /**
    * 计算规则得分
    * @param paramMap paramMap
    * @param flowList 流程列表
    * @return 流程得分
    */
  def calculateRuleScore(paramMap: scala.collection.mutable.Map[String, Int], flowList: Array[FlowCollocation]): Array[FlowScoreResult] = {
    // 封装最终打分结果： flowId 、flowScore 、flowLimitedScore 、是否超过阈值、flowStrategyCode、命中规则列表、命中时间
    val flowScores = new ArrayBuffer[FlowScoreResult]
    //循环数据库查询出来的所有流程，进行匹配打分
    for (flow <- flowList) {
      //拿出当前流程的规则，就是我们web 页面配置的那些阈值
      val ruleList = flow.rules
      //用来封装命中的规则的rileId
      val hitRules = ListBuffer[String]()

      //保存规则计算结果的二维数组（2 行，n 列），第一维是之前streaming 计算统计的结果，第二维是针对对应统计结果的数据库打分结果
      val result = Array.ofDim[Double](2, ruleList.size)
      //根据每个流程对应的规则统计结果与预设的规则进行对比，若统计结果大于预设值，则对应的规则得分有效，否则无效（即设为0）
      var ruleIndex = 0

      //规则是否触发，也就是web 页面的复选框有没有被勾选
      val isTriggered = new ArrayBuffer[Int]()
      //循环数据库规则，循环结束，会将result 填满，hitRules 填满，isTriggered 填满
      for (rule <- ruleList) {
        //规则状态放到这个数组
        isTriggered += rule.ruleStatus
        //规则名字
        val ruleName = rule.ruleName
        //通过规则名字去streaming 统计的结果中找数值
        val streamRuleValue = paramMap.getOrElse(ruleName, 0)

        //把streaming 统计结果封装到第0 行，第ruleIndex 列，后续ruleIndex 会做+1 操作
        result(0)(ruleIndex) = streamRuleValue
        //拿出数据库对应这个规则设置的阈值
        val mysqlRuleValue = if ("criticalPagesLessThanDefault".equals(ruleName)) {
          rule.ruleValue1
        } else {
          rule.ruleValue0
        }
        //数据库对应这个规则的打分
        val ruleScore = rule.ruleScore

        if (streamRuleValue > mysqlRuleValue) {
          //如果streaming 统计结果超过了数据库阈值，将打分记录到result 的第1行，第ruleIndex 列，后续ruleIndex 会做+1 操作
            result(1)(ruleIndex) = ruleScore
          //规则命中，将规则信息添加到数组
          hitRules.append(rule.ruleId)
        } else {
          //没命中，打分设置为0
          result(1)(ruleIndex) = 0
        }

        //ruleIndex 做+1操作，继续对比第二个rule规则
        ruleIndex = ruleIndex + 1
      }

      //计算流程打分，打分区间为：平均分--10*平均分
      val flowScore = calculateFlowScore(result, isTriggered.toArray)
      //命中时间
      val hitTime = new Date().toString

      //（流程Id，流程得分，流程阈值,是否大于阈值，strategyCode，命中规则id 列表，命中时间）  大于阈值定义为爬虫
      flowScores.append(FlowScoreResult(flow.flowId, flowScore, flow.flowLimitScore, flowScore > flow.flowLimitScore, flow.strategyCode, hitRules.toList,hitTime))
    }

    //将所有流程的结果信息返回
    flowScores.toArray
  }


  /**
    * 将满足阈值并且触发了的分数集进行封装
    * @param result
    * @param isTriggered
    * @return
    */
  def calculateFlowScore(result: Array[Array[Double]], isTriggered: Array[Int]): Double = {
    //取出分数
    val score = result(1)
    //创建一个封装Score结果集的arrayBuffer
    val scoresBuffer = new ArrayBuffer[Double]()
    //判断toArray和result的长度要相等
    if(score.length==isTriggered.length){
      //循环isTriggered
      for(i <- 0 until isTriggered.length){
        //判断isTrigger是否触发
        if(isTriggered(i)==0){
          scoresBuffer+=score(i)
        }
      }
    }
    //将arrayBuffer转换成array
    val xa = scoresBuffer.toArray
    //调用算法计算出打分，将得分汇总到arr里，调用算法进行打分，返回Double的分数
    val finalScore:Double = computeScore(score,xa)
    finalScore
  }


  /**
    * 通过算法计算打分
    * 系数2权重：60%，数据区间：10-60
    * 系数3权重：40，数据区间：0-40
    * 系数2+系数3区间为：10-100
    * 系数1为:平均分/10
    * 所以，factor1 * (factor2 + factor3)区间为:平均分--10倍平均分
    * @return
    */
  def computeScore(scores: Array[Double],xa: Array[Double]): Double = {
    //打分列表
    //总打分
    val sum = scores.sum
    //打分列表长度
    val dim = scores.length
    //系数1：平均分/10
    val factor1 = sum / (10 * dim)
    //命中数据库开放规则的score
    //命中规则中，规则分数最高的
    val maxInXa = if (xa.isEmpty) {
      0.0
    } else {
      xa.max
    }
    //系数2：系数2的权重是60，指的是最高score以6为分界，最高score大于6，就给满权重60，不足6，就给对应的maxInXa*10
    val factor2 = if (1 < (1.0 / 6.0) * maxInXa) {
      60
    } else {
      (1.0 / 6.0) * maxInXa * 60
    }
    //系数3：打开的规则总分占总规则总分的百分比，并且系数3的权重是40
    val factor3 = 40 * (xa.sum / sum)

    /**
      * 系数2权重：60%，数据区间：10-60
      * 系数3权重：40，数据区间：0-40
      * 系数2+系数3区间为：10-100
      * 系数1为:平均分/10
      * 所以，factor1 * (factor2 + factor3)区间为:平均分--10倍平均分
      */
    factor1 * (factor2 + factor3)
  }
}


