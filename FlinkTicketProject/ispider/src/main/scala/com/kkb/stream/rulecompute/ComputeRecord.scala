package com.kkb.stream.rulecompute

import java.util.Properties

import com.kkb.stream.common.util.jedis.PropertiesUtil
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import com.kkb.stream.common.bean.{AntiCalculateResult, FlowCollocation, FlowScoreResult, ProcessedData}
import org.apache.flink.api.java.tuple.Tuple

import scala.collection.mutable.ArrayBuffer

object ComputeRecord {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment


    val topicname :String = PropertiesUtil.getStringByKey("target.query.topic","kafkaConfig.properties")
    val servers :String = PropertiesUtil.getStringByKey("bootstrap.servers","kafkaConfig.properties")
    val groupid :String = PropertiesUtil.getStringByKey("group.id","kafkaConfig.properties")


    val props = new Properties()
    props.setProperty("bootstrap.servers",servers)
    props.setProperty("topic.name",topicname)
    props.setProperty("group.id",groupid)

//    val kafkaData: FlinkKafkaConsumer[String] = new FlinkKafkaConsumer[String](topicname,
//      new SimpleStringSchema(),
//      props
//    )

    val kafkaData: FlinkKafkaConsumer[String] = new FlinkKafkaConsumer[String]( topicname,
      new SimpleStringSchema(),
      props)

    kafkaData.setStartFromEarliest()

    val sourceData = env.addSource[String](kafkaData)
    sourceData.map(line=>{
      val arr = line.split("#CS#")
      //返回的类型要跟process 输入的类型一样，否则会报错
      var random = scala.util.Random.nextInt(100)
      var httpType =  "POST"
      if(random >50){
        httpType =  "GET"
      }
      random = scala.util.Random.nextInt(10)
      val Ip = "192.168.92."+random.toString
      (httpType+"_"+Ip,1)
    }).keyBy(_._1)
      .timeWindow(Time.seconds(10))
      .process(new MyProcessWindowFunction())
      .print()
   env.execute()
  }
}
class MyProcessWindowFunction extends ProcessWindowFunction[(String,Int), (String,Int), String, TimeWindow]{
  override def process(key: String, context: Context, elements: Iterable[(String, Int)], out: Collector[(String, Int)]): Unit = {
    val recordlist: List[(String, Int)] = elements.toList

    val sum = recordlist.reduce((a,b)=>{
      (a._1, a._2+b._2)
    })
    out.collect(sum)
  }
}
