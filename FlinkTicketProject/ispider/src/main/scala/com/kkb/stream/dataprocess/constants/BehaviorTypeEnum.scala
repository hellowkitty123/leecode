package com.kkb.stream.dataprocess.constants

/**
  * 操作标记类别 0-查询，1-预定，-1-其他
 */
object BehaviorTypeEnum extends Enumeration{
  type BehaviorTypeEnum = Value
  val Query = Value(0,"Query")
  val Book = Value(1,"Book")
  val Other = Value(-1,"Other")
}
