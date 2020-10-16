package com.kkb.stream.dataprocess.businessProcess

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.kkb.stream.common.bean._
import com.kkb.stream.dataprocess.constants.TravelTypeEnum.TravelTypeEnum
import com.kkb.stream.dataprocess.constants.{BehaviorTypeEnum, FlightTypeEnum, TravelTypeEnum}
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.api.scala._

/**
  * 对数据进行切分，封装成样例类ProcessData
  */
object QueryDataPackage {

  /**
    * 数据数据进行分割，封装样例类ProcessData
    * @param sourceData
    * @return
    */
    def queryDataLoadAndPackage(sourceData: DataStream[String]): DataStream[ProcessedData] = {

    //将数据进行 map，一条条处理
      sourceData.map { sourceLine =>
        //分割数据
        val dataArray = sourceLine.split("#CS#", -1)
        //原始数据，站位，并无数据
        val requestMethod = dataArray(0)
        val request = dataArray(1)
        val remoteAddr = dataArray(2)
        val httpUserAgent = dataArray(3)
        val timeIso8601 = dataArray(4)
        val serverAddr = dataArray(5)
        val isBlackFlag: Boolean = dataArray(6).equalsIgnoreCase("true")
        val requestType: RequestType = RequestType(FlightTypeEnum.withName(dataArray(7)), BehaviorTypeEnum.withName(dataArray(8)))
        val travelType: TravelTypeEnum = TravelTypeEnum.withName(dataArray(9))
        val requestParams: CoreRequestParams = CoreRequestParams(dataArray(10), dataArray(11), dataArray(12))
        val cookieValue_JSESSIONID: String = dataArray(13)
        val cookieValue_USERID: String = dataArray(14)
        //封装 query 数据
        val mapper = new ObjectMapper
        mapper.registerModule(DefaultScalaModule)
        val queryRequestData = mapper.readValue(dataArray(15), classOf[QueryRequestData])

        //分析查询请求的时候不需要 book 数据
        val bookRequestData=null

        //refer来源
        val httpReferrer = dataArray(17)
        //封装流程数据，返回
        ProcessedData(requestMethod, request, remoteAddr, httpUserAgent, timeIso8601, serverAddr, isBlackFlag, requestType, travelType, requestParams, cookieValue_JSESSIONID, cookieValue_USERID, queryRequestData, bookRequestData, httpReferrer)
      }
    }
}
