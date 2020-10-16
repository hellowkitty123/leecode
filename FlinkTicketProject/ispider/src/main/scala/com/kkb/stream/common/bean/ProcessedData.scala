package com.kkb.stream.common.bean

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.kkb.stream.dataprocess.constants.BehaviorTypeEnum.BehaviorTypeEnum
import com.kkb.stream.dataprocess.constants.FlightTypeEnum.FlightTypeEnum
import com.kkb.stream.dataprocess.constants.TravelTypeEnum.TravelTypeEnum

/**
  * 保存请求参数的结构化数据
  * requestMethod：请求方法
  * request：请求路径
  * remoteAddr：客户端ip
  * httpUserAgent：代理
  * timeIso8601：时间
  * serverAddr：请求的服务器地址
  * isBlackFlag：此次请求中的ip地址是否命中高频ip
  * requestType：请求类型
  * travelType：往返类型
  * requestParams：核心请求参数，飞行时间、目的地、出发地
  * cookieValue_JSESSIONID：cookie中的jessionid
  * cookieValue_USERID：cookie中的userid
  * queryRequestData：查询请求的form数据
  * bookRequestData：预定请求的body数据
  * httpReferrer：refer来源
  */
//保存结构化数据
case class ProcessedData(requestMethod: String, request: String,
                         remoteAddr: String, httpUserAgent: String, timeIso8601: String,
                         serverAddr: String, isBlackFlag: Boolean,
                         requestType: RequestType, travelType: TravelTypeEnum,
                         requestParams: CoreRequestParams, cookieValue_JSESSIONID: String, cookieValue_USERID: String,
                         queryRequestData: QueryRequestData, bookRequestData: BookRequestData,
                         httpReferrer: String){

  //用null替换空数据
  implicit class StringUtils(s: String) {
    def repEmptyStr(replacement: String = "NULL"): String = {
      if (s.isEmpty) replacement else s
    }
  }

  //推送到kafka的数据格式，使用#CS#分隔数据
  def toKafkaString(fieldSeparator: String = "#CS#"): String = {

    //转换查询参数和预订参数对象为JSON
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    val queryRequestDataStr = mapper.writeValueAsString(queryRequestData)

    val bookRequestDataStr =  mapper.writeValueAsString(bookRequestData)

      //_1 - 请求类型 GET/POST
      requestMethod.repEmptyStr() + fieldSeparator +
      //_2 - 请求 http://xxxxx
      request.repEmptyStr() + fieldSeparator +
      //_3 - 客户端地址(IP)
      remoteAddr.repEmptyStr() + fieldSeparator +
      //_4 - 客户端浏览器(UA)
      httpUserAgent.repEmptyStr() + fieldSeparator +
      //_5 - 服务器时间的ISO 8610格式
      timeIso8601.repEmptyStr() + fieldSeparator +
      //_6 - 服务器端地址
      serverAddr.repEmptyStr() + fieldSeparator +
      //_7 - 是否属于高频IP段
       isBlackFlag + fieldSeparator +
      //_8 - 航班类型-National/International/Other
      requestType.flightType + fieldSeparator +
      //_9 - 请求行为-Query/Book/Other
      requestType.behaviorType + fieldSeparator +
      //_10 - 行程类型-OneWay/RoundTrip/Unknown
      travelType + fieldSeparator +
      //_11 - 航班日期 -
      requestParams.flightDate.repEmptyStr() + fieldSeparator +
      //_12 - 始发地 -
      requestParams.depcity.repEmptyStr() + fieldSeparator +
      //_13 - 目的地 -
      requestParams.arrcity.repEmptyStr() + fieldSeparator +
      //_14 - 关键Cookie - JSESSIONID
      cookieValue_JSESSIONID.repEmptyStr() + fieldSeparator +
      //_15 - 关键Cookie - 用户ID
      cookieValue_USERID.repEmptyStr() + fieldSeparator +
      //_16 - 解析的查询参数对象JSON
      queryRequestDataStr.repEmptyStr() + fieldSeparator +
      //_17 - 解析的购票参数对象JSON
      bookRequestDataStr.repEmptyStr() + fieldSeparator +
      //_18 - 当前请求是从哪个请求跳转过来的
      httpReferrer.repEmptyStr()

  }
}


//封装切分之后字段的样例类
case class ResultData(request:String,requestMethod:String,contentType:String,requestBody:String,httpReferrer:String,remoteAddr:String,httpUserAgent:String,timeIso8601:String,serverAddr:String,cookiesStr:String,cookieValue_JSESSIONID:String,cookieValue_USERID:String)


//封装请求类型：航线类别（ 0-国内，1-国际，-1-其他）  和 操作类别 （0-查询，1-预定，-1-其他）
case class RequestType(flightType: FlightTypeEnum, behaviorType: BehaviorTypeEnum)


//用于封装核心请求信息：飞行时间、目的地、出发地
case class CoreRequestParams(flightDate: String, depcity: String, arrcity: String)
