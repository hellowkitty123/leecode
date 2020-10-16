package com.kkb.stream.dataprocess.broadcast

import java.util

import com.kkb.stream.common.bean._
import com.kkb.stream.dataprocess.businessProcess._
import com.kkb.stream.dataprocess.constants.TravelTypeEnum.TravelTypeEnum
import com.kkb.stream.dataprocess.constants.{BehaviorTypeEnum, FlightTypeEnum, TravelTypeEnum}
import org.apache.flink.api.common.state.{BroadcastState, MapStateDescriptor, ReadOnlyBroadcastState}
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.util.Collector

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * 自定义BroadcastProcessFunction
  */
class RuleBroadcastProcessFunction extends BroadcastProcessFunction[String,util.HashMap[String,Any],ProcessedData]{

  //定义MapStateDescriptor
  val mapStateDesc = new MapStateDescriptor("air_rule",classOf[Void],classOf[util.HashMap[String,Any]])


  /**
    * Broadcast State在processBroadcastElement方法中进行修改
    * @param map 广播流的数据
    * @param ctx 上下文
    * @param out 输出数据
    */
  override def processBroadcastElement(map: util.HashMap[String, Any], ctx: BroadcastProcessFunction[String, util.HashMap[String, Any], ProcessedData]#Context, out: Collector[ProcessedData]): Unit ={
    //获取广播状态
    val filterRuleBroadCastState: BroadcastState[Void, util.HashMap[String, Any]] = ctx.getBroadcastState(mapStateDesc)

    //清理状态
    filterRuleBroadCastState.clear()

    //更新状态
    filterRuleBroadCastState.put(null,map)
  }

  /**
    * 读取规则，并基于规则，处理事件流中的数据
    * @param message  事件流中的数据
    * @param ctx    上下文
    * @param out    输出数据
    */
  override def processElement(message: String, ctx: BroadcastProcessFunction[String, util.HashMap[String, Any], ProcessedData]#ReadOnlyContext, out: Collector[ProcessedData]): Unit ={
    //todo: 1、获取广播状态
        val ruleBroadCastState: ReadOnlyBroadcastState[Void, util.HashMap[String, Any]] = ctx.getBroadcastState(mapStateDesc)
        val ruleDataMap:  util.HashMap[String, Any] = ruleBroadCastState.get(null)


    //todo: 2、数据的过滤处理
        //匹配标记,默认为 true 表示所有数据都保留
        var save = true

        //对数据进行切分
        val split: Array[String] = message.split("#CS#")

        //取出 request 字段(request 数据为角标为 1 的数据)
        val request = if (split.length > 1) split(1) else ""

        //获取map中过滤规则的key的value,转换为list集合
         val filterRuleIterator= ruleDataMap.get("filterRule").asInstanceOf[ArrayBuffer[String]].toIterator

        while(filterRuleIterator.hasNext && save){
          if(request.matches(filterRuleIterator.next())){
            //匹配到了规则，直接丢弃
            save=false
          }
        }


    //todo: 3、对数据进行脱敏
       //获取过滤之后的数据
       if(save){
            //手机号加密
            val encryptionPhone: String = EncryptionData.encryptionPhone(message)
            //身份证加密
            val encryptionIDPhone: String = EncryptionData.encryptionID(encryptionPhone)


    //todo: 4、对脱敏数据进行分类打标签
          // 4.1 分割脱敏之后的数据
            val splitData: ResultData = DataSplit.splitMessage(encryptionIDPhone)

          //4.2 获取广播中的状态
            //国内查询
            val nationQueryList= ruleDataMap.get("nationQuery").asInstanceOf[ArrayBuffer[String]]
            //国际查询
            val internationQueryList= ruleDataMap.get("internationQuery").asInstanceOf[ArrayBuffer[String]]
            //国内预定
            val nationBookList = ruleDataMap.get("nationBook").asInstanceOf[ArrayBuffer[String]]
            //国际预定
            val internationBookList = ruleDataMap.get("internationBook").asInstanceOf[ArrayBuffer[String]]

          //4.3 数据打标签处理
            //定义一个标识
            var flag=true

            //定义返回的类型
            var requestType:RequestType=null

            //国内查询匹配
            for( regex <- nationQueryList){
              //正则匹配
              if(splitData.request.matches(regex)){
                flag=false

                //打标签
                requestType= RequestType(FlightTypeEnum.National,BehaviorTypeEnum.Query)
              }
            }

            //国际查询匹配
            for(regex <- internationQueryList  if flag){
              //正则匹配
              if(splitData.request.matches(regex)){
                flag=false

                //打标签
                requestType= RequestType(FlightTypeEnum.International,BehaviorTypeEnum.Query)
              }
            }

            //国内预定
            for(regex <- nationBookList  if flag){
              //正则匹配
              if(splitData.request.matches(regex)){
                flag=false

                //打标签
                requestType= RequestType(FlightTypeEnum.National,BehaviorTypeEnum.Book)
              }
            }

            //国际预定
            for(regex <- internationBookList  if flag){
              //正则匹配
              if(splitData.request.matches(regex)){
                flag=false

                //打标签
                requestType= RequestType(FlightTypeEnum.International,BehaviorTypeEnum.Book)
              }
            }

            //都没有匹配到
            if(flag){
              //打标签
              requestType= RequestType(FlightTypeEnum.Other,BehaviorTypeEnum.Other)
            }


    //todo: 5、往返信息打标签
              //获取httpReferrer，用于判断是否是往返
                val httpReferer = splitData.httpReferrer
                  //得到往返标识
                val travelType: TravelTypeEnum.Value = classfyByhttpReferrer(httpReferer)


   //todo:6、解析查询数据
          //获取map中解析的规则数据
            val queryList= ruleDataMap.get("query").asInstanceOf[ArrayBuffer[AnalyzeRule]].toList
            val bookList= ruleDataMap.get("book").asInstanceOf[ArrayBuffer[AnalyzeRule]].toList

       //todo:6.1 解析查询数据
          val queryRequestData: QueryRequestData= AnalyzeRequest.analyzeQueryRequest(requestType,
                                                                                             splitData.requestMethod,
                                                                                             splitData.contentType,
                                                                                             splitData.request,
                                                                                             splitData.requestBody,
                                                                                             travelType,
                                                                                             queryList)

       //todo:6.2 解析预定数据
           val bookRequestData: BookRequestData = AnalyzeBookRequest.analyzeBookRequest(requestType,
                                                                                             splitData.requestMethod,
                                                                                             splitData.contentType,
                                                                                             splitData.request,
                                                                                             splitData.requestBody,
                                                                                             travelType,
                                                                                             bookList)

      //todo:7、数据加工，判断是否为高频ip
          //查询高频ip信息
               val ipBlackList: ArrayBuffer[String] = ruleDataMap.get("ipBlack").asInstanceOf[ArrayBuffer[String]]


         //获取客户端ip
              val ip: String = splitData.remoteAddr
         //判断是否为高频ip
              val isBlackFlag: Boolean = IpOperation.isFreIP(ip,ipBlackList)


     //todo: 8、数据结构化
         val processData: ProcessedData = DataStructured.structuredProcess(splitData.requestMethod,
                                                                           splitData.request,
                                                                           splitData.remoteAddr,
                                                                           splitData.httpUserAgent,
                                                                           splitData.timeIso8601,
                                                                           splitData.serverAddr,
                                                                           isBlackFlag,
                                                                           requestType,
                                                                           travelType,
                                                                           splitData.cookieValue_JSESSIONID,
                                                                           splitData.cookieValue_USERID,
                                                                           queryRequestData,
                                                                           bookRequestData,
                                                                           httpReferer)

        out.collect(processData)

    }

  }

  /**
    * 判断单程和往返
    * @param httpReferrer
    * @return
    */
  def classfyByhttpReferrer(httpReferrer: String) = {
      var dateCounts = 0
      //日期的正则表达式
      val regex = "^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])$"
      //https://b2c.csair.com/B2C40/newTrips/static/main/page/booking/index.html?t=R&c1=CAN&c2=BJS&d1=2020-06-02&d2=2020-06-03&at=1&ct=0&it=0&b1=CAN&b2=PEK-PKX

      if (httpReferrer.contains("?") && httpReferrer.split("\\?").length > 1) {
        //t=R&c1=CAN&c2=BJS&d1=2020-06-02&d2=2020-06-03&at=1&ct=0&it=0&b1=CAN&b2=PEK-PKX
        val params = httpReferrer.split("\\?")(1).split("&")
        for (param <- params) {
          val keyAndValue = param.split("=")
          if (keyAndValue.length > 1 && keyAndValue(1).matches(regex)) {
             //匹配日期郑州表达式
            dateCounts = dateCounts + 1
          }
        }
      }
      if (dateCounts == 1) {
        //单程
        TravelTypeEnum.OneWay
      } else if (dateCounts == 2) {
        //往返
        TravelTypeEnum.RoundTrip
      } else {
        //其他
        TravelTypeEnum.Unknown
      }
    }

}
