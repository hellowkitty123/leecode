package com.kkb.stream

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object TestFileCount {

  def main(args: Array[String]): Unit = {

      val sparkConf = new SparkConf().setAppName("TestFileCount").setMaster("local[2]")

      val sc = new SparkContext(sparkConf)
       sc.setLogLevel("warn")

      val files: RDD[(String, String)] = sc.wholeTextFiles("hdfs://hadoop001:8020/wc")

      files.map(x=>(x._1,x._2.split("\n").length)).foreach(println)

     sc.stop()



  }
}
