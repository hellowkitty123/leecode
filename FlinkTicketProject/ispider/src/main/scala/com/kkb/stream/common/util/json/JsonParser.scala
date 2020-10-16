package com.kkb.stream.common.util.json

import com.kkb.stream.common.bean.{AnalyzeRule, BookRequestData}

import scala.util.parsing.json.JSON


object JsonParser {

  /**
    * 将json转成map
    *
    * @param json json字串
    */
  def parseJsonToMap(json: String): Map[String, String] = {
    var paramMap = scala.collection.immutable.Map[String, String]()
    val parseJson = JSON.parseFull(json)
    parseJson match {
      case Some(map: Map[String, String]) => paramMap = map
      case None => println("Parsing failed")
      case other => println("Unknown data structure: " + other)
    }
    paramMap
  }

  /**
    * 根据json数据解析出来的结果构建Bean对象
    *
    * @param requestBody 报文
    * @param analyzeRule 规则
    * @return AnalyzedData数据
    */
  def parseJsonToBean(requestBody: String, analyzeRule: AnalyzeRule): BookRequestData = {
    var resultData: BookRequestData = new BookRequestData
    //去掉json字符串的前后缀
    val jsonStringTemp = requestBody.replace("\\x22", "").trim
    val jsonString = JsonPathParser(jsonStringTemp)
    //解析购票人ID
    resultData.bookUserId = jsonString.getValueByPath[String](analyzeRule.book_bookUserId) match {
      case Some(id) => id
      case None => ""
    }
    //解析乘机人姓
    val firstName = jsonString.getValueByPath[java.util.ArrayList[String]](analyzeRule.book_psgFirName) match {
      case Some(fn) => fn
      case None => null
    }
    //解析乘机人名
    val lastName = jsonString.getValueByPath[java.util.ArrayList[String]](analyzeRule.book_psgName) match {
      case Some(ln) => ln
      case None => null
    }
    //构建乘机人姓名
    if (firstName != null) {
      for (i <- 0 until firstName.size()) {
        val psgName = firstName.get(i) + lastName.get(i)
        resultData.psgName += psgName
      }
    }
    //解析证件类型
    val idTypes = jsonString.getValueByPath[java.util.ArrayList[String]](analyzeRule.book_idType) match {
      case Some(temp) => temp
      case None => null
    }
    if (idTypes != null) {
      for (i <- 0 until idTypes.size()) {
        resultData.idType += idTypes.get(i)
      }
    }
    //解析乘机人证件号
    val numbers = jsonString.getValueByPath[java.util.ArrayList[String]](analyzeRule.book_idCard) match {
      case Some(temp) => temp
      case None => null
    }
    if (numbers != null) {
      for (i <- 0 until numbers.size()) {
        resultData.idCard += numbers.get(i)
      }
    }
    //解析联系人姓名
    jsonString.getValueByPath[String](analyzeRule.book_contractName) match {
      case Some(name) => resultData.contractName = name
      case None => ""
    }
    //解析联系人手机号
    resultData.contractPhone = jsonString.getValueByPath[String](analyzeRule.book_contractPhone) match {
      case Some(mobile) => mobile
      case None => ""
    }

    //始发地
    val depCities = jsonString.getValueByPath[String](analyzeRule.book_depCity) match {
      case Some(depCity) => depCity
      case None => null
    }
    if (depCities != null) {
      resultData.depCity += depCities
    }

    //到达地
    val arrCities = jsonString.getValueByPath[String](analyzeRule.book_arrCity) match {
      case Some(arrCity) => arrCity
      case None => null
    }
    if (arrCities != null) {
      resultData.arrCity += arrCities
    }

    //起飞时间
    val flightDates = jsonString.getValueByPath[String](analyzeRule.book_flightDate) match {
      case Some(flightDate) => flightDate
      case None => null
    }
    if (flightDates != null) {
      resultData.flightDate += flightDates
    }

    //航班号
    val flightNos = jsonString.getValueByPath[String](analyzeRule.book_flightNo) match {
      case Some(flightNo) => flightNo
      case None => null
    }
    if (flightNos != null) {
      resultData.flightNo += flightNos
    }

    //仓位级别
    val cabins = jsonString.getValueByPath[String](analyzeRule.book_cabin) match {
      case Some(cabin) => cabin
      case None => null
    }
    if (cabins != null) {
      resultData.cabin += cabins
    }

    resultData
  }
}