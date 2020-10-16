package com.kkb.batch.function

import java.util.UUID
import org.apache.flink.table.functions.ScalarFunction

//自定义UDF函数生成唯一标识
class UniqueFlag extends ScalarFunction {

  def eval(): String = {
     UUID.randomUUID().toString
  }

}
