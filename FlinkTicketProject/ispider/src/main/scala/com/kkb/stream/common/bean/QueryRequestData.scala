package com.kkb.stream.common.bean

import com.kkb.stream.dataprocess.constants.FlightTypeEnum


/**
 * 查询请求参数集
 * 通过GET/POST提交的通过解析URL参数或解析FORM表单或解析FORM表单中的JSON获取
 * 该对象通过JSON形式传输
 */
class QueryRequestData extends Serializable{

  //是有解析成功存有有效数据
  var isEmpty = true

  //航班类型
  var flightType:Int = FlightTypeEnum.Other.id

  //是否往返 - 国内通过httpRefer识别，国际通过Post表单数据
  var travelType = -1


  //以下参数通过请求原始数据获取，不需要从表单数据中解析
  //订票时间
  //购票人ID - 会员登录标识 - 例如明珠会员卡号
  var queryTime = ""
  var queryUserId = ""
  //购票人ID - 非会员登录标识 - 非会员手机号
  var queryUnUserId = ""
  //订票IP
  var queryIp = ""

  //以下参数需要从请求参数中解析获得
  //始发地
  var depCity = ""
  //目的地
  var arrCity = ""
  //起飞时间
  var flightDate = ""
  //成人乘机人数
  var adultNum = ""
  //儿童乘机人数
  var childNum = ""
  //婴儿乘机人数
  var infantNum = ""

  //国际航班查询特有字段
  //国家
  var country = ""

}
