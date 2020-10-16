package com.kkb.stream.dataprocess.launch

import java.sql.Connection
import java.{lang, util}
import java.util.Properties

import com.kkb.stream.common.bean._
import com.kkb.stream.common.util.database.{C3p0Test, C3p0Util}
import com.kkb.stream.common.util.jedis.PropertiesUtil
import com.kkb.stream.dataprocess.broadcast.RuleBroadcastProcessFunction
import com.kkb.stream.dataprocess.businessProcess.BusinessProcess
import com.kkb.stream.dataprocess.constants.BehaviorTypeEnum
import com.kkb.stream.dataprocess.mysqlsource.MysqlRuleSource
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.streaming.api.scala.{BroadcastConnectedStream, DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer, KafkaSerializationSchema}
import org.apache.flink.api.scala._
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.kafka.clients.producer.ProducerRecord

/**
  * 程序执行的驱动类入口,该类中会调用大量的方法来实现数据处理的逻辑
  */
object DataProcessLaunch {
  def main(args: Array[String]): Unit = {

    //todo: 1、构建flink实时处理的环境
      val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


    //开启checkpoint
    //checkpoint配置
//      env.enableCheckpointing(5000)
//      env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
//      env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
//      env.getCheckpointConfig.setCheckpointTimeout(60000)
//      env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
//      env.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
//      //设置statebackend
//      env.setStateBackend(new FsStateBackend("hdfs://hadoop001:8020/flink_kafka/checkpoints",true))

    //todo: 2、获取kafka相关配置
      //获取kafka集群地址
        val bootstrapServers: String = PropertiesUtil.getStringByKey("bootstrap.servers","kafkaConfig.properties")
      //获取topic名称
        val topicName: String = PropertiesUtil.getStringByKey("source.nginx.topic","kafkaConfig.properties")
      //获取消费者组id
        val groupID: String = PropertiesUtil.getStringByKey("group.id","kafkaConfig.properties")
      //flink自动检测topic新增分区
       val partitionDiscovery: String = PropertiesUtil.getStringByKey("flink.partition-discovery.interval-millis","kafkaConfig.properties")

      //构建配置对象
      val properties = new Properties()
          properties.setProperty("bootstrap.servers",bootstrapServers)
          properties.setProperty("topic.name",topicName)
          properties.setProperty("group.id",groupID)
          properties.setProperty("flink.partition-discovery.interval-millis",partitionDiscovery)


    //todo: 3、构建kafka消费者
      val kafkaConsumer: FlinkKafkaConsumer[String] = new FlinkKafkaConsumer[String]( topicName,
                                                                                    new SimpleStringSchema(),
                                                                                    properties)
      //指定从最新的数据开始消费
      kafkaConsumer.setStartFromEarliest()

    //todo: 4、添加source数据源
      val sourceData: DataStream[String] = env.addSource(kafkaConsumer)
//    sourceData.print()
    //todo: 5、对数据进行处理——>链路统计功能
      BusinessProcess.linkCount(sourceData)


//    //todo: 6、flink读取从数据库中读取规则数据，数据类型HashMap[String, Any]
        val ruleStream: DataStream[util.HashMap[String, Any]] = env.addSource(new MysqlRuleSource)
//        ruleStream.print()
//    //todo：7、创建MapStateDescriptor
      //MapStateDescriptor定义了状态的名称、Key和Value的类型。
      //这里MapStateDescriptor中，key是Void类型，value是Map<String, String>类型。
        val mapStateDesc = new MapStateDescriptor("air_rule",classOf[Void],classOf[Map[String,Any]])
//
//    //todo: 8、将规则数据流广播，形成广播流
        val ruleBroadcastStream: BroadcastStream[util.HashMap[String, Any]] = ruleStream.broadcast(mapStateDesc)
//
//
//    //todo: 9、事件流和广播的配置流连接，形成BroadcastConnectedStream
        val ruleBroadcastConnectedStream: BroadcastConnectedStream[String, util.HashMap[String, Any]] = sourceData.connect(ruleBroadcastStream)
//
//
//    //todo: 10、对BroadcastConnectedStream应用process方法，根据配置(规则)处理事件
        val structureDataStream: DataStream[ProcessedData] = ruleBroadcastConnectedStream.process(new RuleBroadcastProcessFunction)
        structureDataStream.print()
//
//
//    //todo: 11、数据推送到kafka中
//      //todo: 11.1 推送query查询数据准备
        val queryDataStream: DataStream[String] = structureDataStream.filter(message => {
           message.requestType.behaviorType == BehaviorTypeEnum.Query
        }).map(message => message.toKafkaString())
//
//      //todo: 11.2 推送book预定数据准备
       val bookDataStream: DataStream[String] = structureDataStream.filter(message => {
          message.requestType.behaviorType == BehaviorTypeEnum.Book
      }).map(message => message.toKafkaString())
//
//
//      //todo: 11.3 查询数据写入kafka
          val queryProperties = new Properties()
        //topic名称
          val queryTopicName: String = PropertiesUtil.getStringByKey("target.query.topic","kafkaConfig.properties")
        //获取kafka集群地址
           val kafkaServers: String = PropertiesUtil.getStringByKey("bootstrap.servers","kafkaConfig.properties")

          queryProperties.setProperty("bootstrap.servers", kafkaServers)

       //添加到sink
         queryDataStream.addSink(new FlinkKafkaProducer[String](
                                     queryTopicName,
                                     new CustomSerializationSchema(queryTopicName),
                                     queryProperties,
                                     FlinkKafkaProducer.Semantic.EXACTLY_ONCE
          ))
//
//
//    //todo: 11.4 预定数据写入kafka
      val bookProperties = new Properties()
      //topic名称
        val bookTopicName: String = PropertiesUtil.getStringByKey("target.book.topic","kafkaConfig.properties")

        bookProperties.setProperty("bootstrap.servers", kafkaServers)

       //添加到sink
        bookDataStream.addSink(new FlinkKafkaProducer[String](
                                      bookTopicName,
                                      new CustomSerializationSchema(bookTopicName),
                                      bookProperties,
                                      FlinkKafkaProducer.Semantic.EXACTLY_ONCE
          ))



    //todo:启动flink程序
    env.execute("DataProcessLaunch")
  }
}


/**
  * 自定义KafkaSerializationSchema
  * @param topic
  */
class  CustomSerializationSchema(topic:String)  extends KafkaSerializationSchema[String]{
  override def serialize(element: String, timestamp: lang.Long): ProducerRecord[Array[Byte], Array[Byte]] = {
     new ProducerRecord[Array[Byte], Array[Byte]](topic,element.getBytes)
  }
}


