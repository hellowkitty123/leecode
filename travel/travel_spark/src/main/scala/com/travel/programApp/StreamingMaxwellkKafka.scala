package com.travel.programApp

import com.travel.common.{ConfigUtil, Constants}
import com.travel.utils.HbaseTools
import com.travel.utils.{HbaseTools, JsonParse}
import org.apache
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.HasOffsetRanges
import org.apache.spark.{SparkConf, sql}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingMaxwellkKafka {
  def main(args: Array[String]): Unit = {
    val brokers = ConfigUtil.getConfig(Constants.KAFKA_BOOTSTRAP_SERVERS)
    val maxwellConf = new SparkConf().setMaster("local[4]").setAppName("maxwell")
    val session = new apache.spark.sql.SparkSession.Builder().config(maxwellConf).getOrCreate()
    val streamingContext: StreamingContext = new StreamingContext(session.sparkContext,Seconds(1));
    val group: String = "vech_group"
    val kafkaParams: Map[String, Object] = Map[String,Object](
    "bootstrap.servers" ->brokers,
      "key.deserializer"->classOf[StringDeserializer],
      "value.deserializer"->classOf[StringDeserializer],
      "group.id"->group,
      "auto.offset.reset"->"earliest",
      "enable.auto.commit"->(false:java.lang.Boolean)
    )
    val topics: Array[String] = Array(Constants.VECHE)

    val matchPattern:String = "veche"
    val getDataFromKafka = HbaseTools.getStreamingContextFromHBase(streamingContext, kafkaParams, topics, group, matchPattern)
    getDataFromKafka.foreachRDD(eachRDD=>{
      if (!eachRDD.isEmpty()){

        eachRDD.foreachPartition(eachpartition=>{
          val conn = HbaseTools.getHbaseConn
          eachpartition.foreach(eachLine =>{
            val jsonstr = eachLine.value()
            val parse = JsonParse.parse(jsonstr)
            HbaseTools.saveBusinessDatas(parse._1,parse,conn)
          })
          HbaseTools.closeConn(conn)
        })
      }
      val ranges = eachRDD.asInstanceOf[HasOffsetRanges].offsetRanges
      for (eachRange <- ranges){
        val offset = eachRange.fromOffset
        val partition = eachRange.partition
        val topic = eachRange.topic
        val endoffset = eachRange.untilOffset
        HbaseTools.saveBatchOffset(group,topic,partition.toString,endoffset)
      }
    })
    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
