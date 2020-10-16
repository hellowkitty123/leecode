package com.kkb.stream.common.util.xml

import java.io.StringReader
import javax.xml.parsers.{DocumentBuilder, DocumentBuilderFactory}
import javax.xml.xpath.{XPath, XPathConstants, XPathFactory}

import com.kkb.stream.common.bean.{AnalyzeRule, BookRequestData}
import com.kkb.stream.dataprocess.constants.TravelTypeEnum
import com.kkb.stream.dataprocess.constants.TravelTypeEnum.TravelTypeEnum
import org.w3c.dom.{Document, NodeList}
import org.xml.sax.InputSource

object xmlUtil {
  /**
    * 解析xml - 多个节点
    *
    * @param xmlString   xml字符串
    * @param nodeNameMap xml中对应多个节点的xpath表达式
    * @return 返回一个map
    */
  def parseXmlToMap(xmlString: String, nodeNameMap: Map[String, String]): scala.collection.mutable.Map[String, String] = {
    //去掉xml头信息
    val xmlSubString = xmlString.substring(xmlString.indexOf("<page>"))
    val resultMap = scala.collection.mutable.Map[String, String]()
    val xmlBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val is = new InputSource(new StringReader(xmlSubString))
    val xmlDocument: Document = xmlBuilder.parse(is)
    val xpath: XPath = XPathFactory.newInstance().newXPath()

    nodeNameMap.keys.foreach { key =>

      val expression = xpath.compile(nodeNameMap(key))
      val nodeList = expression.evaluate(xmlDocument, XPathConstants.NODESET).asInstanceOf[NodeList]

      for (node <- 0 until nodeList.getLength) {
        val nodeValue = nodeList.item(node).getNodeValue
        resultMap += (key + node -> nodeValue)
        println(key + node + " -----------> " + nodeValue)
      }
    }
    resultMap
  }

  /**
    * 解析xml文件
    *
    * @param xmlString   xml数据
    * @param analyzeRule 规则对象
    * @return 规则数据
    */
  def parseXML(xmlString: String, analyzeRule: AnalyzeRule): BookRequestData = {
    //返回值
    val analyzedData = new BookRequestData
    //xml字符串
    val xmlSubString = xmlString.substring(xmlString.indexOf("<page>"))
    val xmlBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val is = new InputSource(new StringReader(xmlSubString))
    //xml对象模型
    val xmlDocument: Document = xmlBuilder.parse(is)
    val xpath: XPath = XPathFactory.newInstance().newXPath()
    //用于判断乘客的数量
    //val sn = "//SEATNUM/text()"
    //val seatNum = xpath.evaluate(sn, xmlDocument)

    //从对象模型中解析出bookTime
    /*if(analyzeRule.bookTime != null && !analyzeRule.bookTime.equals("")){
      val bookTime = xpath.evaluate(analyzeRule.bookTime, xmlDocument)
      analyzedData.bookTime = bookTime
    }*/
    //从对象模型中解析出bookUserId
    if (analyzeRule.book_bookUserId != null && !analyzeRule.book_bookUserId.equals("")) {
      val bookUserId = xpath.evaluate(analyzeRule.book_bookUserId, xmlDocument)
      analyzedData.bookUserId = bookUserId
    }
    //从对象模型中解析出bookUnUserId
    if (analyzeRule.book_bookUnUserId != null && !analyzeRule.book_bookUnUserId.equals("")) {
      val bookUnUserId = xpath.evaluate(analyzeRule.book_bookUnUserId, xmlDocument)
      analyzedData.bookUnUserId = bookUnUserId
    }
    //从对象模型中解析出bookIp
    /*if(analyzeRule.bookIp != null && !analyzeRule.bookIp.equals("")){
      val bookIp = xpath.evaluate(analyzeRule.bookIp, xmlDocument)
      analyzedData.bookIp = bookIp
    }*/
    //从对象模型中解析出psgName,可以存在多个值
    val psgName = parseForXpath(analyzeRule.book_psgName, xpath, xmlDocument)
    if (psgName != null && psgName.size > 0) {
      psgName.foreach { temp =>
        analyzedData.psgName += temp
      }
    }
    //从对象模型中解析出psgType,可以存在多个值
    val psgType = parseForXpath(analyzeRule.book_psgType, xpath, xmlDocument)
    if (psgType != null && psgType.size > 0) {
      psgType.foreach { temp =>
        analyzedData.psgType += temp
      }
    }

    //从对象模型中解析出idType,可以存在多个值
    val idType = parseForXpath(analyzeRule.book_idType, xpath, xmlDocument)
    if (idType != null && idType.size > 0) {
      idType.foreach { temp =>
        analyzedData.idType += temp
      }
    }

    //从对象模型中解析出idCard,可以存在多个值
    val idCard = parseForXpath(analyzeRule.book_idCard, xpath, xmlDocument)
    if (idCard != null && idCard.size > 0) {
      idCard.foreach { temp =>
        analyzedData.idCard += temp
      }
    }

    //从对象模型中解析出contractName
    if (analyzeRule.book_contractName != null && !analyzeRule.book_contractName.equals("")) {
      val contractName = xpath.evaluate(analyzeRule.book_contractName, xmlDocument)
      analyzedData.contractName = contractName.substring(0, contractName.indexOf("|"))
    }
    //从对象模型中解析出contractPhone
    if (analyzeRule.book_contractPhone != null && !analyzeRule.book_contractPhone.equals("")) {
      val contractPhone = xpath.evaluate(analyzeRule.book_contractPhone, xmlDocument)
      analyzedData.contractPhone = contractPhone.substring(0, contractPhone.indexOf("/"))
    }
    //从对象模型中解析出bookAgent
    /*if(analyzeRule.bookAgent != null && !analyzeRule.bookAgent.equals("")){
      val bookAgent = xpath.evaluate(analyzeRule.bookAgent, xmlDocument)
      analyzedData.bookAgent = bookAgent
    }*/

    //从对象模型中解析出depCity,可以存在一个或两个值
    val depCity = parseForXpath(analyzeRule.book_depCity, xpath, xmlDocument)
    if (depCity != null && depCity.size > 0) {
      depCity.foreach { temp =>
        analyzedData.depCity += temp
      }
    }

    //从对象模型中解析出arrCity,可以存在一个或两个值
    val arrCity = parseForXpath(analyzeRule.book_arrCity, xpath, xmlDocument)
    if (arrCity != null && arrCity.size > 0) {
      arrCity.foreach { temp =>
        analyzedData.arrCity += temp
      }
    }

    //从对象模型中解析出flightDate,可以存在一个或两个值
    val flightDate = parseForXpath(analyzeRule.book_flightDate, xpath, xmlDocument)
    if (flightDate != null && flightDate.size > 0) {
      flightDate.foreach { temp =>
        analyzedData.flightDate += temp
      }
    }

    //从对象模型中解析出cabin,可以存在一个或两个值
    val cabin = parseForXpath(analyzeRule.book_cabin, xpath, xmlDocument)
    if (cabin != null && cabin.size > 0) {
      cabin.foreach { temp =>
        analyzedData.cabin += temp
      }
    }

    analyzedData
  }

  /**
    * 解析对应的字段
    *
    * @param param       String字符串数据
    * @param xpath       xpath路径
    * @param xmlDocument xml文件
    * @return list集合
    */
  def parseForXpath(param: String, xpath: XPath, xmlDocument: Document): scala.collection.mutable.ListBuffer[String] = {
    var result = scala.collection.mutable.ListBuffer[String]()
    if (param != null && !param.equals("")) {
      val Exp = xpath.compile(param)
      val nodeList = Exp.evaluate(xmlDocument, XPathConstants.NODESET).asInstanceOf[NodeList]
      for (node <- 0 until nodeList.getLength) {
        val nodeValue = nodeList.item(node).getNodeValue
        result += nodeValue
      }
      result
    } else {
      null
    }
  }

  /**
    * 用于判断单程或往返
    *
    * @param xmlString xml数据
    * @return TravelTypeEnum枚举
    */
  def getTravelType(xmlString: String): TravelTypeEnum = {
    var travelType = TravelTypeEnum.Unknown
    //xml字符串
    if (xmlString.contains("<page>")) {
      val xmlSubString = xmlString.substring(xmlString.indexOf("<page>"), xmlString.indexOf("</page>") + "</page>".length)
      val xmlBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
      val is = new InputSource(new StringReader(xmlSubString))
      //xml对象模型
      val xmlDocument: Document = xmlBuilder.parse(is)
      val xpath: XPath = XPathFactory.newInstance().newXPath()
      //解析xml中SEGTYPE字段
      val st = "//SEGTYPE/text()"
      val segType = xpath.evaluate(st, xmlDocument)
      travelType = segType match {
        case "S" => TravelTypeEnum.OneWay
        case "R" => TravelTypeEnum.RoundTrip
        case _ => TravelTypeEnum.Unknown
      }
    }
    travelType
  }
}
