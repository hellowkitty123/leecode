package com.kkb.stream.common.bean

import com.kkb.stream.dataprocess.constants.FlightTypeEnum


/**
 * 预定请求数据解析结果类
 */
class BookRequestData  extends Serializable{
  //航班类型
  var flightType: Int = FlightTypeEnum.Other.id
  //是否往返
  var travelType = -1
  //订票时间(国内，国际-访问时间)
  var bookTime = ""
  //购票人ID - 会员登录标识
  var bookUserId = ""
  //购票人ID - 非会员登录标识
  var bookUnUserId = ""
  //订票IP（国内，国际-访问地址）
  var bookIp = ""
  //乘机人名（国内-解析xml，国际-json）
  var psgName = scala.collection.mutable.ListBuffer[String]()
  //乘机人类型， 例如成人、儿童、婴儿（国内-解析xml，国际-解析json）
  var psgType = scala.collection.mutable.ListBuffer[String]()
  //证件类型，例如身份证、护照、其他等（国内-解析xml，国际-解析json）
  var idType = scala.collection.mutable.ListBuffer[String]()
  //乘机人证件号（国内-解析xml，国际-解析json）
  var idCard = scala.collection.mutable.ListBuffer[String]()
  //联系人名（国内-解析xml，国际-解析json）
  var contractName = ""
  //联系人手机号（国内-解析xml，国际-解析json）
  var contractPhone = ""
  //销售单位
  var bookAgent = ""
  //始发地
  var depCity = scala.collection.mutable.ListBuffer[String]()
  //目的地
  var arrCity = scala.collection.mutable.ListBuffer[String]()
  //起飞时间
  var flightDate = scala.collection.mutable.ListBuffer[String]()
  //航班号
  var flightNo = scala.collection.mutable.ListBuffer[String]()
  //仓位级别
  var cabin = scala.collection.mutable.ListBuffer[String]()
}
