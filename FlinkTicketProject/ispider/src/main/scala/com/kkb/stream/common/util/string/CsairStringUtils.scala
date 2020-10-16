package com.kkb.stream.common.util.string

/**
 * 业务字符串处理工具
 */
object CsairStringUtils {
  /**
   * Cookie字符串字符清洗
   * 替换段落中的多个空格、换行、制表符
    * @param str 传进来的字符串
    * @return 处理好的字符串
   */
    def trimSpacesChars(str: String): String = {
      val res = str.replaceAll("(\0|\\s*|\r|\n)", "")
      res
    }
}
