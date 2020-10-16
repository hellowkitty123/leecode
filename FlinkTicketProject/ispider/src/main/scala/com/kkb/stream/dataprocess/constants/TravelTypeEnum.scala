package com.kkb.stream.dataprocess.constants

/**
 * 标记往返类别 0-单程，1-往返，-1-其他
 */
object TravelTypeEnum extends Enumeration {
  //这里仅仅是为了将Enumration.Value的类型暴露出来给外界使用而已
  type TravelTypeEnum = Value
  val OneWay = Value(0)
  val RoundTrip = Value(1)
  val Unknown  = Value(-1)
}
