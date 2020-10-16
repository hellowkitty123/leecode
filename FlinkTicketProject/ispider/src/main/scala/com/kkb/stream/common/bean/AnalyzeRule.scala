package com.kkb.stream.common.bean

import com.kkb.stream.dataprocess.constants.{BehaviorTypeEnum, FlightTypeEnum}


/**
 * 解析规则类：对应表analyzerule
 */
class AnalyzeRule extends Serializable{
  //解析规则ID
  var id = ""
  //航班类型
  var flightType: Int = FlightTypeEnum.Other.id
  //操作类型 - 查询、预订
  var BehaviorType = BehaviorTypeEnum.Other.id

  //解析规则URL匹配的正则表达式
  var requestMatchExpression = ""
  //解析规则匹配的请求方法 GET/POST
  var requestMethod = ""
  //是常规的GET请求传参 - 参数在URL请求中 ?c1=1&c2=2&c3=3
  var isNormalGet = false
  //是常规的POST请求传参 - 参数在request_body中的键值对,标准FORM表单数据,Content-Type为application/x-www-form-urlencoded
  var isNormalForm = false
  //JSON数据通过Post提交，Content-Type为application/json ，通过Ajax提交的Json数据
  var isApplicationJson = false
  //XML数据通过Post提交，Content-Type为Text/XML ，通过Ajax提交的XML数据
  var isTextXml = false
  //是JSON数据格式传参
  var isJson = false
  //是XML数据格式传参
  var isXML = false
  //JSON或XML数据在FORM表单数据中的KEY值 - isNormalForm=false，isNormalForm=false时，即数据为通过Form提交的Json或XML数据，需要从Form根据Key提取Json再做解析
  var formDataField = ""

  //MARK - 以下为采集字段对应解析规则，如果为GET或标准POST FORM表单，则为对应Key名称，如果XML则为XPATH，如果为JSON则为JsonPath
  //购票解析规则
  //购票人ID - 会员登录标识 - 例如明珠会员卡号
  var book_bookUserId = ""
  //购票人ID - 非会员登录标识 - 非会员手机号
  var book_bookUnUserId = ""
  //乘机人名(国内-姓名合一，国际-姓名分开)
  var book_psgName = ""
  //乘机人类型， 例如成人、儿童、婴儿
  var book_psgType = ""
  //证件类型，例如身份证、护照、其他等
  var book_idType = ""
  //乘机人证件号
  var book_idCard = ""
  //联系人名
  var book_contractName = ""
  //联系人手机号
  var book_contractPhone = ""
  //始发地
  var book_depCity = ""
  //目的地
  var book_arrCity = ""
  //起飞时间
  var book_flightDate = ""
  //航班号
  var book_flightNo = ""
  //仓位级别
  var book_cabin = ""

  //查询解析规则
  //以下参数需要从请求参数中解析获得
  //始发地
  var query_depCity = ""
  //目的地
  var query_arrCity = ""
  //起飞时间
  var query_flightDate = ""

  //成人乘机人数
  var query_adultNum = ""
  //儿童乘机人数
  var query_childNum = ""
  //婴儿乘机人数
  var query_infantNum = ""

  //国际航班查询特有字段
  //国际-国家
  var query_country = ""
  //国际-是否往返 - 国内通过httpRefer识别，国际通过Post表单数据
  var query_travelType = ""
  //国际-乘机人姓名中的姓
  var book_psgFirName = ""
}
