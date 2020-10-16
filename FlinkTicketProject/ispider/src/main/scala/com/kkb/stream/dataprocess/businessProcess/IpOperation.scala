package com.kkb.stream.dataprocess.businessProcess

import scala.collection.mutable.ArrayBuffer

//判断高频ip
object IpOperation {


  /**
    * 判断该ip是否属于高频ip
    *
    * @param ip
    * @param ipFileds
    * @return
    */
  def isFreIP(ip: String, ipFileds: ArrayBuffer[String]): Boolean = {
          //初始化标记
          var resultFlag = false

          val it = ipFileds.iterator
          //遍历
          while (it.hasNext && !resultFlag) {

            val mysqlIpBlack = it.next()

             //判断
            if (mysqlIpBlack.equals(ip)) {
              //修改配置
              resultFlag = true
            }
          }
          //返回标记
          resultFlag
  }
}

