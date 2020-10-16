package com.kkb.stream.dataprocess.constants

/**
  * 标记航线类别 0-国内，1-国际，-1-其他
 */

object FlightTypeEnum extends Enumeration{
  type FlightTypeEnum = Value
  val National = Value(0)
  val International = Value(1)
  val Other = Value(-1)

}
