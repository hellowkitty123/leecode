package com.kkb.batch.function

import org.apache.flink.table.functions.ScalarFunction

//自定义UDF函数判断用户请求类型
class RequestFlag extends ScalarFunction {

  def eval(request: String): String = {

    if (request.matches("^.*/bookingnew/.*$") //预定、商城
      || request.matches("^.*/bookingGroup/.*$")
      || request.matches("^.*/ita/intl/zh/shop/.*$")) {
      "1"
    } else if (request.matches("^.*/modules/permissionnew/.*$") //权限、乘客信息
      || request.matches("^.*/ita/intl/zh/passengers/.*$")) {
      "2"
    } else if (request.matches("^.*upp_payment/pay/.*$")) {
      //支付
      "3"
    } else {
      //其他，查询等操作
      "0"
    }

  }

}
