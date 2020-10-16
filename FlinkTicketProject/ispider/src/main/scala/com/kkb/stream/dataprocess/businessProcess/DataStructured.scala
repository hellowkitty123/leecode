package com.kkb.stream.dataprocess.businessProcess

import com.kkb.stream.common.bean._
import com.kkb.stream.dataprocess.constants.{BehaviorTypeEnum, TravelTypeEnum}

/**
  * 数据的结构化处理业务类
  */
object DataStructured {


  /**
    * 数据结构化方法
    * @param requestMethod          请求方式
    * @param request                请求url
    * @param remoteAddr             客户端地址
    * @param httpUserAgent          游览器信息
    * @param timeIso8601            请求时间
    * @param serverAddr             服务端地址
    * @param isBlackFlag            是否属于高频ip
    * @param requestType            请求类型 ，查询、预定、国内、国际
    * @param travelType             是否往返
    * @param cookieValue_JSESSIONID  cookie中的jsessionid
    * @param cookieValue_USERID      cookie中的userid
    * @param queryRequestData       解析查询数据封装
    * @param bookRequestData        解析预定数据封装
    * @param httpReferer            跳转连接
    * @return
    */

  def structuredProcess(requestMethod: String,
                        request: String,
                        remoteAddr: String,
                        httpUserAgent: String,
                        timeIso8601: String,
                        serverAddr: String,
                        isBlackFlag: Boolean,
                        requestType: RequestType,
                        travelType: TravelTypeEnum.Value,
                        cookieValue_JSESSIONID: String,
                        cookieValue_USERID: String,
                        queryRequestData: QueryRequestData,
                        bookRequestData: BookRequestData,
                        httpReferer: String):ProcessedData = {

    //定义航班时间
    var flightDate = ""
    //定义出发地
    var depCity = ""
    //定义到达地
    var arrCity = ""

    //从解析查询封装数据中匹配
    if(requestType.behaviorType ==  BehaviorTypeEnum.Query){

      flightDate = queryRequestData.flightDate
      depCity = queryRequestData.depCity
      arrCity = queryRequestData.arrCity

      //从解析预定封装数据中匹配
    }else if(requestType.behaviorType ==  BehaviorTypeEnum.Book){

      flightDate = bookRequestData.flightDate.mkString
      depCity = bookRequestData.depCity.mkString
      arrCity = bookRequestData.arrCity.mkString

    }

    //主要请求参数
    val requestParams = CoreRequestParams(flightDate, depCity, arrCity)

    ProcessedData(requestMethod, request, remoteAddr, httpUserAgent, timeIso8601, serverAddr, isBlackFlag, requestType, travelType, requestParams, cookieValue_JSESSIONID, cookieValue_USERID, queryRequestData, bookRequestData, httpReferer)

  }

}
