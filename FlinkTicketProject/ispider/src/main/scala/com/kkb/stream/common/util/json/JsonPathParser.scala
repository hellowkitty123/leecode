package com.kkb.stream.common.util.json

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.Configuration
import scala.reflect.ClassTag


class JsonPathParser(document: Object) {
  /**
   * 根据json节点路径来获取对应的值，路径写法参考：https://github.com/json-path/JsonPath
   * 因为所返回的值类型根据传入的节点不同而不同，所以调用者必须知道json结构以及明确指定返回的对象的类型
    * @param pathFormat 模式
    *  @return option[TYPE]对象
   */
  def getValueByPath[TYPE: ClassTag](pathFormat: String): Option[TYPE] = {
    try {
      Some(JsonPath.read(document, pathFormat).asInstanceOf[TYPE])
    } catch {
      case e: Exception =>
        //        e.printStackTrace()
        None
    }
  }
}

/**
  * 解析json数据
  */
object JsonPathParser {
  def apply(json: String): JsonPathParser = {
    try{
    	val document = Configuration.defaultConfiguration().jsonProvider().parse(json)
    	new JsonPathParser(document)
    }catch{
      case e: Exception =>
        e.printStackTrace()
        null
    }
  }

}