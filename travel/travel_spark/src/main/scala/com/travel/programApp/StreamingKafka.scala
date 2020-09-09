package com.travel.programApp

import java.lang

import com.travel.common.{ConfigUtil, Constants, JedisUtil}
import com.travel.utils.HbaseTools
import org.apache.hadoop.hbase.client.Connection
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.HasOffsetRanges
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingKafka {
  def main(args: Array[String]): Unit = {
    val brokers = ConfigUtil.getConfig(Constants.KAFKA_BOOTSTRAP_SERVERS)
    val topics = Array(ConfigUtil.getConfig(Constants.CHENG_DU_GPS_TOPIC), ConfigUtil.getConfig(Constants.HAI_KOU_GPS_TOPIC))
    val group:String = "gps_consum_group"
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> group,
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: lang.Boolean)
    )
    val sparkConf = new SparkConf().setMaster("local[4]").setAppName("sparkkafka")
    val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    val sc = sparkSession.sparkContext
    sc.setLogLevel("WARN")

    val streamingContext = new StreamingContext(sc, Seconds(1))
    val result: InputDStream[ConsumerRecord[String, String]] = HbaseTools.getStreamingContextFromHBase(streamingContext, kafkaParams, topics, group, "(.*)gps_topic")
    result.foreachRDD(eachRdd =>{
        if (!eachRdd.isEmpty()){

            val conn: Connection = HbaseTools.getHbaseConn
            val jedis = JedisUtil.getJedis
            eachRdd.foreach(eachline=>{
              HbaseTools.saveToHBaseAndRedis(conn,jedis,eachline)
            })
            JedisUtil.returnJedis(jedis)
            conn.close()
        }
      val ranges = eachRdd.asInstanceOf[HasOffsetRanges].offsetRanges
      for (eachrange <- ranges) {
        val startoffset: Long = eachrange.fromOffset
        val endoffset: Long = eachrange.untilOffset
        val topic: String = eachrange.topic
        val partition: Int = eachrange.partition
        HbaseTools.saveBatchOffset(group, topic, partition + "", endoffset)
      }
    })
    streamingContext.start()
    streamingContext.awaitTermination()





  }
}
