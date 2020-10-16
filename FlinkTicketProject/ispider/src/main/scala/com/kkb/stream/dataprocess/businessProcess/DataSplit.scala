package com.kkb.stream.dataprocess.businessProcess

import java.util.regex.Pattern

import com.kkb.stream.common.bean.ResultData
import com.kkb.stream.common.util.decode.{EscapeToolBox, RequestDecoder}
import com.kkb.stream.common.util.jedis.PropertiesUtil
import com.kkb.stream.common.util.string.CsairStringUtils

/**
  * 数据分割
  */
object DataSplit {

  def splitMessage(message:String)={

    //用#CS#分割数据
    val values = message.split("#CS#", -1)
    //记录数据长度
    val valuesLength = values.length
    //request 原始数据
    val regionalRequest = if (valuesLength > 1) values(1) else ""
    //分割出 request 中的 url
    val request = if (regionalRequest.split(" ").length > 1) { regionalRequest.split(" ")(1) } else { "" }
    //请求方式 GET/POST
    val requestMethod = if (valuesLength > 2) values(2) else ""
    //content_type
    val contentType = if (valuesLength > 3) values(3) else ""
    //Post 提交的数据体
    val requestBody = if (valuesLength > 4) values(4) else ""
    //http_referrer
    val httpReferrer = if (valuesLength > 5) values(5) else ""
    //客户端 IP
    val remoteAddr = if (valuesLength > 6) values(6) else ""
    //客户端 UA
    val httpUserAgent = if (valuesLength > 7) values(7) else ""
    //服务器时间的 ISO8610 格式
    val timeIso8601 = if (valuesLength > 8) values(8) else ""
    //服务器地址
    val serverAddr = if (valuesLength > 9) values(9) else ""
    //Cookie 信息
    // 原始信息中获取 Cookie 字符串，去掉空格，制表符
      //JSESSIONID=782121159357B98CA6112554CF44321E; sid=b5cc11e02e154ac5b0f3609332f86803; aid=8ae8768760927e280160bb348bef3e12; identifyStatus=N; userType4logCookie=M; userId4logCookie=D1279C812D20ABDB6612BA7AF1C5D13C; useridCookie=D1279C812D20ABDB6612BA7AF1C5D13C; userCodeCookie=D1279C812D20ABDB6612BA7AF1C5D13C; temp_zh=cou%3D0%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2018-01-13%3B%E5%B9%BF%E5%B7%9E-%E5%8C%97%E4%BA%AC%3B1%2C0%2C0%3B%26cou%3D1%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2020-06-01%3B%E5%B9%BF%E5%B7%9E-%E6%88%90%E9%83%BD%3B1%2C0%2C0%3B%26; JSESSIONID=782121159357B98CA6112554CF44321E; WT-FPC=id=211.103.142.26-608782688.30635197:lv=1516170718655:ss=1516170709449:fs=1513243317440:pn=2:vn=10; language=zh_CN; WT.al_flight=WT.al_hctype(S)%3AWT.al_adultnum(1)%3AWT.al_childnum(0)%3AWT.al_infantnum(0)%3AWT.al_orgcity1(CAN)%3AWT.al_dstcity1(CTU)%3AWT.al_orgdate1(2020-06-01)
    val cookiesStr = CsairStringUtils.trimSpacesChars(if (valuesLength > 10) values(10) else "")

    //提取 Cookie 信息并保存为 K-V 形式
    val cookieMap = {
        var tempMap = new scala.collection.mutable.HashMap[String, String]
        if (!cookiesStr.equals("")) {
          cookiesStr.split(";").foreach { s =>
             val kv = s.split("=")
            //UTF8 解码
            if (kv.length > 1) {
              try {
                  val chPattern = Pattern.compile("u([0-9a-fA-F]{4})")
                  val chMatcher = chPattern.matcher(kv(1))
                  var isUnicode = false
                  while (chMatcher.find()) {
                    isUnicode = true
                  }
                  if (isUnicode) {
                    tempMap += (kv(0) -> EscapeToolBox.unescape(kv(1)))
                  } else {
                    tempMap += (kv(0) -> RequestDecoder.decodePostRequest(kv(1)))
                  }
              } catch {
                case e: Exception => e.printStackTrace()
              }
            }
           }
        }
      tempMap
    }

    //Cookie 关键信息解析
    // 从配置文件读取 Cookie 配置信息
    val cookieKey_JSESSIONID = PropertiesUtil.getStringByKey("cookie.JSESSIONID.key", "cookieConfig.properties")
    val cookieKey_userId4logCookie = PropertiesUtil.getStringByKey("cookie.userId.key", "cookieConfig.properties")
    //Cookie-JSESSIONID
    val cookieValue_JSESSIONID = cookieMap.getOrElse(cookieKey_JSESSIONID, "NULL")
    //Cookie-USERID-用户 ID
    val cookieValue_USERID = cookieMap.getOrElse(cookieKey_userId4logCookie, "NULL")

     //封装数据进行返回
    ResultData(request,requestMethod,contentType,requestBody,httpReferrer,remoteAddr,httpUserAgent,timeIso8601,serverAddr,cookiesStr,cookieValue_JSESSIONID,cookieValue_USERID)

  }
}
