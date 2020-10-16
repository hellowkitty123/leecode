package com.kkb.stream.rulecompute

import java.util
import java.util.Properties
import com.kkb.stream.common.bean.{AntiCalculateResult, FlowScoreResult, ProcessedData}
import com.kkb.stream.common.util.jedis.PropertiesUtil
import com.kkb.stream.dataprocess.broadcast.ProcessRuleBroadcastProcessFunction
import com.kkb.stream.dataprocess.businessProcess.QueryDataPackage
import com.kkb.stream.dataprocess.mysqlsource.MysqlProcessRuleSource
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.streaming.api.scala.{BroadcastConnectedStream, DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink
import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer
import java.time.ZoneId


/**
  * 指标统计的驱动主类
  */
object ComputeLauncher {

  def main(args: Array[String]): Unit = {

    //todo: 1、构建flink实时处理的环境
        val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //todo: 2、获取kafka相关配置
      //获取kafka集群地址
        val bootstrapServers: String = PropertiesUtil.getStringByKey("bootstrap.servers","kafkaConfig.properties")
      //获取topic名称
        val topicName: String = PropertiesUtil.getStringByKey("target.query.topic","kafkaConfig.properties")
      //获取消费者组id
        val groupID: String = PropertiesUtil.getStringByKey("rule.group.id","kafkaConfig.properties")

        val properties = new Properties()
        properties.setProperty("bootstrap.servers",bootstrapServers)
        properties.setProperty("topic.name",topicName)
        properties.setProperty("group.id",groupID)



    //todo: 3、构建kafka消费者
      val kafkaConsumer: FlinkKafkaConsumer[String] = new FlinkKafkaConsumer[String]( topicName,
                                                                                      new SimpleStringSchema(),
                                                                                      properties)
      //指定从最新的数据开始消费
      kafkaConsumer.setStartFromEarliest()

    //todo: 4、添加source数据源
      val sourceData: DataStream[String] = env.addSource(kafkaConsumer)


    //todo: 5、对数据进行切分，封装成样例类ProcessData
      val processedDataStream: DataStream[ProcessedData] = QueryDataPackage.queryDataLoadAndPackage(sourceData)


    //todo: 6、flink读取从数据库中读取规则数据，，数据类型HashMap[String, Any]
      val ruleStream: DataStream[util.HashMap[String, Any]] = env.addSource(new MysqlProcessRuleSource)
       //ruleStream.print()

    //todo：7、创建MapStateDescriptor
      //MapStateDescriptor定义了状态的名称、Key和Value的类型。
      //这里MapStateDescriptor中，key是Void类型，value是Map<String, String>类型。
      val mapStateDesc = new MapStateDescriptor("processRule",classOf[Void],classOf[Map[String,Any]])

    //todo: 8、将规则数据流广播，形成广播流
      val processRuleBroadcastStream: BroadcastStream[util.HashMap[String, Any]] = ruleStream.broadcast(mapStateDesc)

    //todo: 9、事件流和广播的配置流连接，形成BroadcastConnectedStream
      val connectedStream: BroadcastConnectedStream[ProcessedData, util.HashMap[String, Any]] = processedDataStream.connect(processRuleBroadcastStream)

    //todo: 10、对BroadcastConnectedStream应用process方法，更新广播状态
      val processedStream: DataStream[(ProcessedData, util.HashMap[String, Any])] = connectedStream.process(new ProcessRuleBroadcastProcessFunction)


    //todo: 11、指标统计
      val antiCalculateResultDataStream: DataStream[AntiCalculateResult] = CoreRule.processTarget(processedStream)


    //todo: 12、提取黑名单
      val ipBlackDataStream: DataStream[AntiCalculateResult] = antiCalculateResultDataStream.filter(x => {
      //提取黑名单
      val flowScoreResults = x.flowsScore.filter(y => y.isUpLimited)

      //判断黑名单非空
      flowScoreResults.nonEmpty
    })


    //todo: 13、黑名单保存redis
    val result: DataStream[(String, FlowScoreResult)] = ipBlackDataStream.flatMap(x => {
        //一个ip可能匹配到多个流程
        val flowScoreResultsArray: Array[FlowScoreResult] = x.flowsScore
        //封装结果 (ip,FlowScoreResult)
        val array: Array[(String, FlowScoreResult)] = flowScoreResultsArray.map(flowScoreResult => (x.ip, flowScoreResult))
        println(array)
        array
    })

    // 使用我们自定义的sink,保存结果到redis中
      val redisSink: RedisSink[(String, FlowScoreResult)] = CoreRule.getRedisSink()
      result.addSink(redisSink)


    //todo: kafka数据备份到HDFS
    val outputPath=PropertiesUtil.getStringByKey("kafkaToHdfsPath","HDFSPathConfig.properties")

   val hdfsSink: BucketingSink[String] = new BucketingSink[String](outputPath)
       //使用东八区时间格式"yyyy-MM-dd"命名存储区
      hdfsSink.setBucketer(new DateTimeBucketer[String]("yyyy-MM-dd", ZoneId.of("Asia/Shanghai")))
      // 下述两种条件满足其一时，创建新的块文件
        // 条件1.设置块大小为100MB
      hdfsSink.setBatchSize(100 * 1024 * 1024) // this is 100 MB
        // 条件2.设置时间间隔10s
      hdfsSink.setBatchRolloverInterval(10*1000) // this is 10s
        // 设置块文件前缀
      hdfsSink.setPendingPrefix("")
        // 设置块文件后缀
      hdfsSink.setPendingSuffix("")
        // 设置运行中的文件前缀
      hdfsSink.setInProgressPrefix(".")

      //添加sink
      sourceData.addSink(hdfsSink)

    // /outputPath/{date-time}/part-{parallel-task}-{count}
    // date-time我们从日期/时间格式获取的字符串，parallel-task是并行接收器实例的索引，count是由于批处理大小创建的块文件的运行数。


    //todo:启动flink程序
      env.execute("ComputeLauncher")
  }
}

