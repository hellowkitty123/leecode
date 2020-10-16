package com.kkb.batch.function

import org.apache.flink.table.functions.ScalarFunction

//自定义UDF函数判断爬虫
class SpiderFlag extends ScalarFunction {

  def eval(flowID: String): String = {

    if (flowID == null || "null".equalsIgnoreCase(flowID)) {
      //非爬虫
      "0"
    } else {
      //爬虫
      "1"
    }

  }

}
