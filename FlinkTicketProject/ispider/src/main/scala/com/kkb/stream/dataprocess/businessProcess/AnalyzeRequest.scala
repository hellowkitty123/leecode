package com.kkb.stream.dataprocess.businessProcess

import java.io.StringReader
import java.util
import javax.xml.parsers.{DocumentBuilder, DocumentBuilderFactory}
import javax.xml.xpath.{XPath, XPathFactory}

import com.kkb.stream.common.bean.{AnalyzeRule, QueryRequestData, RequestType}
import com.kkb.stream.common.util.decode.RequestDecoder
import com.kkb.stream.common.util.json.JsonPathParser
import com.kkb.stream.dataprocess.constants.TravelTypeEnum.TravelTypeEnum
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger
import org.w3c.dom.Document
import org.xml.sax.InputSource

/**
  * 解析查询数据
  */
object AnalyzeRequest {
  val logger = Logger.getLogger("AnalyzeRequest")
  /**
    * 不同类型数据，根据不同解析规则进行解析
    */
  def analyzeQueryRequest(requestTypeLabel: RequestType,
                          requestMethod: String,
                          contentType: String,
                          request: String,
                          requestBody: String,
                          travelType: TravelTypeEnum,
                          analyzeRules: List[AnalyzeRule]): QueryRequestData= {

    //创建用来封装query数据的类QueryRequestData
    val queryRequestData: QueryRequestData = new QueryRequestData
    //数据库中有四条解析规则，我们需要通过传过来的这一条数据，确定数据匹配上的解析规则，然后用这个解析规则解析数据
    val matchedRules = analyzeRules.filter { rule =>
      //先根据请求方式和请求类型过滤出是属于查询的规则
      if (rule.requestMethod.equalsIgnoreCase(requestMethod) && rule.BehaviorType == requestTypeLabel.behaviorType.id)
        true
      else
        false
    }.filter { rule =>
      //然后根据url正则表达式，过滤出是属于国内查询还是国际查询
      request.matches(rule.requestMatchExpression)
    }
    //如果过滤结果是大于0个，证明此条数据已经找到匹配的解析规则,不同类型数据根据规则解析数据
    if (matchedRules.size > 0) {
      //如果匹配到多个获取最后一个匹配成功的解析规则
      val matchedRule = matchedRules.last

      //解析普通GET方式提交的参数
      if (matchedRule.requestMethod.equalsIgnoreCase("GET") && matchedRule.isNormalGet) {
        analyseNormalGetParams(queryRequestData, request, matchedRule)

      //解析普通Form表单方式提交的参数
      } else if (matchedRule.requestMethod.equalsIgnoreCase("POST") && matchedRule.isNormalForm) {
        analyseNormalFormParams(queryRequestData, requestBody, matchedRule)

      //解析post提交的、非form表单、json类型的数据
      } else if (matchedRule.requestMethod.equalsIgnoreCase("POST") && !matchedRule.isNormalForm
        && matchedRule.isJson && matchedRule.formDataField.nonEmpty) {
        analyseUnNormalFormParams(queryRequestData, requestBody, matchedRule)

        //对xml数据进行解析并进行标识
      } else if (matchedRule.requestMethod.equalsIgnoreCase("POST") && contentType.equalsIgnoreCase("text/xml") &&
        matchedRule.isTextXml && matchedRule.isXML && requestBody.nonEmpty) {
        analyseTextXmlParams(queryRequestData, requestBody, matchedRule)
      }
    }

    //封装操作类型和航线类型
      queryRequestData.flightType = requestTypeLabel.behaviorType.id
      queryRequestData.travelType = travelType.id
      queryRequestData

  }

  /**
    * 解析通过json方式提交的其他形式参数
    *
    * @param queryRequestData
    * @param requestBody
    * @param matchedRule
    * @return
    */
  def analyseUnNormalFormParams(queryRequestData: QueryRequestData,
                                requestBody: String,
                                matchedRule: AnalyzeRule): QueryRequestData = {
    try {
      val paramMap = new util.HashMap[String, String]()
      //先用&分割出所有的kv对
      val params = requestBody.split("&")
      //将kv对用=分割，封装到map中
      for (param <- params) {
        val keyAndValue = param.split("=")
        if (keyAndValue.length > 1) {
          paramMap.put(keyAndValue(0) , RequestDecoder.decodePostRequest(keyAndValue(1)))
        }
      }

      //获取Json字符串
      val json: String = paramMap.get(matchedRule.formDataField)

    if(StringUtils.isNotBlank(json)){
      //根据解析规则提取Json字符串信息
      extractGeneralJsonData(json, queryRequestData, matchedRule)
    }

    } catch {
      case e: Exception => e.printStackTrace()
        logger.error("解析Form表单数据出错，请求为：" + requestBody)
    }
    //返回数据
    queryRequestData
  }
  /**
    * 测试规则数据
    *
    * @param queryRequestData
    * @param requestBody
    * @param matchedRule
    * @return
    */
  def analyseApplicationJsonParams(queryRequestData: QueryRequestData, requestBody: String, matchedRule: AnalyzeRule) = Option[QueryRequestData] {

    //获取Json字符串
    val json = requestBody
    //根据解析规则提取Json字符串信息
    extractGeneralJsonData(json, queryRequestData, matchedRule)
    //返回数据
    queryRequestData
  }

  /**
    * 对xml数据进行解析并进行标识
    *
    * @param queryRequestData
    * @param requestBody
    * @param matchedRule
    */
  def analyseTextXmlParams(queryRequestData: QueryRequestData, requestBody: String, matchedRule: AnalyzeRule) = {

    //xml字符串
    val xmlSubString = requestBody.substring(requestBody.indexOf("<page>"))
    val xmlBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val is = new InputSource(new StringReader(xmlSubString))
    //xml对象模型
    val xmlDocument: Document = xmlBuilder.parse(is)
    val xpath: XPath = XPathFactory.newInstance().newXPath()

    //成人乘机人数
    if (!matchedRule.query_adultNum.trim.isEmpty) {
      queryRequestData.adultNum = xpath.evaluate(matchedRule.query_adultNum, xmlDocument) match {
        case value if value != null && (!value.trim.isEmpty) => value
        case _ => ""
      }
    }
    //目的地
    if (!matchedRule.query_arrCity.trim.isEmpty) {
      queryRequestData.arrCity = xpath.evaluate(matchedRule.query_arrCity, xmlDocument) match {
        case value if value != null && (!value.trim.isEmpty) => value
        case _ => ""
      }
    }
    //儿童乘机人数
    if (!matchedRule.query_childNum.trim.isEmpty) {
      queryRequestData.childNum = xpath.evaluate(matchedRule.query_childNum, xmlDocument) match {
        case value if value != null && (!value.trim.isEmpty) => value
        case _ => ""
      }
    }
    //国家
    if (!matchedRule.query_country.trim.isEmpty) {
      queryRequestData.country = xpath.evaluate(matchedRule.query_country, xmlDocument) match {
        case value if value != null && (!value.trim.isEmpty) => value
        case _ => ""
      }
    }
    //始发地
    if (!matchedRule.query_depCity.trim.isEmpty) {
      queryRequestData.depCity = xpath.evaluate(matchedRule.query_depCity, xmlDocument) match {
        case value if value != null && (!value.trim.isEmpty) => value
        case _ => ""
      }
    }
    //起飞时间
    if (!matchedRule.query_flightDate.trim.isEmpty) {
      queryRequestData.flightDate = xpath.evaluate(matchedRule.query_flightDate, xmlDocument) match {
        case value if value != null && (!value.trim.isEmpty) => value
        case _ => ""
      }
    }
    //婴儿乘机人数
    if (!matchedRule.query_infantNum.trim.isEmpty) {
      queryRequestData.infantNum = xpath.evaluate(matchedRule.query_infantNum, xmlDocument) match {
        case value if value != null && (!value.trim.isEmpty) => value
        case _ => ""
      }
    }

    //国际航班特有单程往返标识
    if (!matchedRule.query_travelType.trim.isEmpty) {
      queryRequestData.travelType = xpath.evaluate(matchedRule.query_travelType, xmlDocument) match {
        case value if value != null && (!value.trim.isEmpty) => value.toInt
        case _ => -1
      }
    }
    //标识解析数据成功
    queryRequestData.isEmpty = false

  }



  /**
    * 解析普通Form表单方式提交的参数
    * (application/x-www-form-urlencoded)
    *
    * @param queryRequestData
    * @param requestBody
    * @param matchedRule
    * @return
    */
  def analyseNormalFormParams(queryRequestData: QueryRequestData,
                              requestBody: String,
                              matchedRule: AnalyzeRule): QueryRequestData = {
    //解析requestBody，其中中文汉字用utf-8编码，所以这里需要对其进行解码
    val paramMap = scala.collection.mutable.Map[String, String]()
    val params = requestBody.split("&")
    for (param <- params) {
      val keyAndValue = param.split("=")
      if (keyAndValue.length > 1) {
        paramMap += (keyAndValue(0) -> RequestDecoder.decodePostRequest(keyAndValue(1)))
      }
    }

    getQueryRequestFromMap(queryRequestData, paramMap, matchedRule)
  }



  /**
    * 解析普通GET方式提交的参数
    *
    * @param queryRequestData
    * @param request
    * @param matchedRule
    * @return
    */
  def analyseNormalGetParams(queryRequestData: QueryRequestData,
                             request: String,
                             matchedRule: AnalyzeRule): QueryRequestData = {

    //get请求参数集合
    val paramMap = scala.collection.mutable.Map[String, String]()
    //获取请求中包含的所有参数
    if (request.contains("?") && request.split("\\?").length > 1) {
      val params = request.split("\\?")(1).split("&")
      for (param <- params) {
        val keyAndValue = param.split("=")
        if (keyAndValue.length > 1) {
          paramMap += (keyAndValue(0) -> keyAndValue(1))
        }
      }
    }

    getQueryRequestFromMap(queryRequestData, paramMap, matchedRule)
  }

  /**
    * 对国际航班特有单程往返进行标识
    *
    * @param queryRequestData
    * @param paramMap
    * @param matchedRule
    * @return
    */
  def getQueryRequestFromMap(queryRequestData: QueryRequestData,
                             paramMap: scala.collection.mutable.Map[String, String],
                             matchedRule: AnalyzeRule): QueryRequestData = {
    if (paramMap.nonEmpty) {
      //成人乘机人数
      queryRequestData.adultNum = paramMap.getOrElse(matchedRule.query_adultNum, "")
      //目的地
      queryRequestData.arrCity = paramMap.getOrElse(matchedRule.query_arrCity, "")
      //儿童乘机人数
      queryRequestData.childNum = paramMap.getOrElse(matchedRule.query_childNum, "")
      //国家
      queryRequestData.country = paramMap.getOrElse(matchedRule.query_country, "")
      //始发地
      queryRequestData.depCity = paramMap.getOrElse(matchedRule.query_depCity, "")
      //起飞时间
      queryRequestData.flightDate = paramMap.getOrElse(matchedRule.query_flightDate, "")
      //婴儿乘机人数
      queryRequestData.infantNum = paramMap.getOrElse(matchedRule.query_infantNum, "")

      //国际航班特有单程往返标识
      if (!matchedRule.query_travelType.trim.isEmpty) {
        paramMap.get(matchedRule.query_travelType) match {
          case Some(param) => {
            if (!param.trim.isEmpty) {
              queryRequestData.travelType = param.toInt
            } else {
              queryRequestData.travelType = -1
            }
          }
          case None => queryRequestData.travelType = -1
        }
      }
      //标识解析数据成功
      queryRequestData.isEmpty = false
    }
    queryRequestData
  }



  /**
    * 通用查询参数Json字符串数据提取方法
    *
    * @param json
    * @param queryRequestData
    * @param matchedRule
    */
  def extractGeneralJsonData(json: String, queryRequestData: QueryRequestData, matchedRule: AnalyzeRule): Unit = {
  //json串不为空
    if (!json.trim.isEmpty) {
      //解析json串
      val jsonPathParser = JsonPathParser(json)
      //成人乘机人数
      queryRequestData.adultNum = jsonPathParser.getValueByPath[String](
        matchedRule.query_adultNum) match {
        case Some(result) => result
        case None => ""
      }
      //目的地
      queryRequestData.arrCity = jsonPathParser.getValueByPath[String](
        matchedRule.query_arrCity) match {
        case Some(result) => result
        case None => ""
      }
      //儿童乘机人数
      queryRequestData.childNum = jsonPathParser.getValueByPath[String](
        matchedRule.query_childNum) match {
        case Some(result) => result
        case None => ""
      }
      //国家
      queryRequestData.country = jsonPathParser.getValueByPath[String](
        matchedRule.query_country) match {
        case Some(result) => result
        case None => ""
      }
      //始发地
      queryRequestData.depCity = jsonPathParser.getValueByPath[String](
        matchedRule.query_depCity) match {
        case Some(result) => result
        case None => ""
      }
      //起飞时间
      queryRequestData.flightDate = jsonPathParser.getValueByPath[String](
        matchedRule.query_flightDate) match {
        case Some(result) => result
        case None => ""
      }
      //婴儿乘机人数
      queryRequestData.infantNum = jsonPathParser.getValueByPath[String](
        matchedRule.query_infantNum) match {
        case Some(result) => result
        case None => ""
      }
      //标识解析数据成功
      queryRequestData.isEmpty = false
    }

  }
  def main(args: Array[String]) {
    val json = "{\"depcity\":\"CAN\", \"arrcity\":\"WUH\", \"flightdate\":\"20180220\", \"adultnum\":\"1\", \"childnum\":\"0\", \"infantnum\":\"0\", \"cabinorder\":\"0\", \"airline\":\"1\", \"flytype\":\"0\", \"international\":\"0\", \"action\":\"0\", \"segtype\":\"1\", \"cache\":\"0\", \"preUrl\":\"\", \"isMember\":\"\"}"
    val jsonPathParser = JsonPathParser(json)
    println(jsonPathParser)
    println(jsonPathParser.getValueByPath[String]("$.flightdate"))

  }
}