package com.kkb.stream.common.util.decode

import java.net.URLDecoder


object RequestDecoder {
  /**
   * 解码POST请求中，request_body中的json数据
    *@param encoded 解码串
   */
  def decodePostRequest(encoded: String): String = {
    val decoded = URLDecoder.decode(encoded,"utf-8")
    decoded
  }

}
