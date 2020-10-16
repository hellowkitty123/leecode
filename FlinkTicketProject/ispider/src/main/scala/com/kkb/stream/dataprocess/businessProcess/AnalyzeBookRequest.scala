package com.kkb.stream.dataprocess.businessProcess

import com.kkb.stream.common.bean.{AnalyzeRule, BookRequestData, RequestType}
import com.kkb.stream.common.util.decode.{MD5, RequestDecoder}
import com.kkb.stream.common.util.json.JsonParser
import com.kkb.stream.common.util.xml.xmlUtil
import com.kkb.stream.dataprocess.constants.FlightTypeEnum
import com.kkb.stream.dataprocess.constants.TravelTypeEnum.TravelTypeEnum

/**
  * 解析预定数据
  */
object AnalyzeBookRequest {
  /**
   * 将数据进行分析，得出规则
    *
   * @param requestTypeLabel
   * @param requestMethod
   * @param contentType
   * @param request
   * @param requestBody
   * @param travelType
   * @return
   */
  def analyzeBookRequest(requestTypeLabel: RequestType,
                         requestMethod: String,
                         contentType: String,
                         request: String,
                         requestBody: String,
                         travelType: TravelTypeEnum,
                         analyzeRules: List[AnalyzeRule]): BookRequestData = {

    var resultData: BookRequestData = new BookRequestData
    //循环规则，匹配出符合数据的规则
    val matchedRules = analyzeRules.filter { rule =>
      //匹配请求方式和请求类型 - 预定
      if (rule.requestMethod.equalsIgnoreCase(requestMethod) && rule.BehaviorType == requestTypeLabel.behaviorType.id)
        true
      else
        false
    }.filter { rule =>
      request.matches(rule.requestMatchExpression)
    }
    //匹配出规则，解析
    if (matchedRules.size > 0) {
      val matchedRule = matchedRules.last
      //国内
      val nationalId = FlightTypeEnum.National.id
      //国际
      val internationalId = FlightTypeEnum.International.id
      //xml类型数据
      if (contentType.equalsIgnoreCase("text/xml") && matchedRule.isTextXml && matchedRule.isXML && requestBody.nonEmpty) {
        resultData = xmlUtil.parseXML(requestBody, matchedRule)
      }
      //json类型数据
      else if (contentType.equalsIgnoreCase("application/json") && !matchedRule.isNormalForm && matchedRule.isApplicationJson && matchedRule.isJson && requestBody.nonEmpty) {
        resultData = JsonParser.parseJsonToBean(requestBody, matchedRule)
      }
      //json类型数据
      else if (!matchedRule.isNormalForm && matchedRule.isJson && !matchedRule.isApplicationJson && matchedRule.formDataField.nonEmpty) {
        val paramMap = scala.collection.mutable.Map[String, String]()
        val params = requestBody.split("&")
        for (param <- params) {
          val keyAndValue = param.split("=")
          if (keyAndValue.length > 1) {
            paramMap += (keyAndValue(0) -> RequestDecoder.decodePostRequest(keyAndValue(1)))

          }
        }
        var json = paramMap.getOrElse(matchedRule.formDataField, "")
        if ("[".equals(json.charAt(0).toString()) && "]".equals(json.charAt(json.length() - 1).toString())) {
          json = json.substring(1, json.length() - 1)
        }
        if (json.trim.nonEmpty) {
          resultData = JsonParser.parseJsonToBean(json, matchedRule)
        }
      }

      matchedRule.flightType match {
        //国内
        case `nationalId` => {
          resultData.flightType = FlightTypeEnum.National.id
          resultData.travelType = travelType.id
        }
        //国际
        case `internationalId` => {
          resultData.flightType = FlightTypeEnum.International.id
          resultData.travelType = travelType.id
        }
        case _ => None
      }
    }
      //加密手机号，证件号。
      val md5 = new MD5()
      resultData.contractPhone = md5.getMD5ofStr(resultData.contractPhone)
      val idCardEncrypted = scala.collection.mutable.ListBuffer[String]()

      for (record <- resultData.idCard) {
        idCardEncrypted.append(md5.getMD5ofStr(record))
      }

      resultData.idCard = idCardEncrypted

    //返回
      resultData

  }
}
