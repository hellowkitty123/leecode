package com.kkb.stream.mock

import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem


/**
  * 测试数据改造
  */
object MockTestData {

  def main(args: Array[String]): Unit = {

     val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
     val data: DataSet[String] = env.readTextFile("D:\\test\\part-00000")
     var i=0

      val result: DataSet[String] = data.map(x => {
        var y = x
        if (i % 50 == 0) {
          y = y.replace("/B2C40/query/jaxb/direct/query.ao", "/ita/intl/zh/shop/csair")
        }
        if (i % 201 == 0) {
          y = y.replace("/B2C40/query/jaxb/direct/query.ao",
            "/modules/permissionnew/csair").replace("192.168.56.1", "243.234.12.43")
        }
        if (i % 701 == 0) {
          y = y.replace("/B2C40/query/jaxb/direct/query.ao", "/modules/permissionnew/csair")
        }
        if (i % 1001 == 0) {
          y = y.replace("/B2C40/query/jaxb/direct/query.ao", "upp_payment/pay/csair")
        }
        if (i % 2001 == 0) {
          y = y.replace("192.168.56.1", "243.234.12.43")
        }
        if (i % 200 == 0 || i % 402 == 0 || i % 2002 == 0 || i % 502 == 0) {
          y = y.replace("National", "Internatinal")
        }
        i = i + 1
        y
      }
      )

     //以文本文件保存结果数据
    result.writeAsText("D:\\mock\\part-00000",FileSystem.WriteMode.OVERWRITE).setParallelism(1)


    //启动
    env.execute("MockTestData")

  }
}
