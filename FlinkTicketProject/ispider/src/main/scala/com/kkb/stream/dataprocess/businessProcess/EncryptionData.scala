package com.kkb.stream.dataprocess.businessProcess

import java.util.regex.{Matcher, Pattern}

import com.kkb.stream.common.util.decode.MD5

/**
  * 加密手机号和身份证
  */
object EncryptionData {

   //加密手机号
  def encryptionPhone(message:String):String={
   //定义MD5加密类
    val md5 = new MD5

   //定义返回的结果,后面通过加密之后重新赋值
    var encrypted= message

    //定义手机号的正则表达式
    val phonePattern = Pattern.compile("((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[0-9])|(18[0,5-9]))\\d{8}")

    //匹配
    val phoneMatcher: Matcher = phonePattern.matcher(message)

    //查找手机号    可能会出现这种情况  11231213608861234987
    while(phoneMatcher.find()){
      //手机号的前一个 index，注：phoneMatcher.group()找到的手机号
      val beforeIndex = message.indexOf(phoneMatcher.group()) - 1
      //手机号的后一个 index
      val afterIndex = beforeIndex + phoneMatcher.group().length() + 1
      //手机号的前一个字符
      val beforeLetter = message.charAt(beforeIndex).toString()

      //如果前一位字符不是数字，那就要看后一位是否是数字
      if (!beforeLetter.matches("^[0-9]$")) {
        //判断后一位字符
          if (afterIndex < message.length()) {
            //手机号的后一个字符
              val afterLetter = message.charAt(afterIndex).toString()

              //后一位也不是数字，那说明这个字符串就是一个电话号码
                if (!afterLetter.matches("^[0-9]$")) {
                    encrypted = encrypted.replace(phoneMatcher.group(), md5.getMD5ofStr(phoneMatcher.group()))
                }
            } else {
                    encrypted = encrypted.replace(phoneMatcher.group(), md5.getMD5ofStr(phoneMatcher.group()))
               }
            }

    }
    //返回加密结果
    encrypted


  }


  //加密身份证号
  def encryptionID(message:String):String={
    //定义MD5加密类
    val md5 = new MD5

    //定义返回的结果,后面通过加密之后重新赋值
    var encrypted= message

    //定义手机号的正则表达式
    val idPattern = Pattern.compile("(\\d{18})|(\\d{17}(\\d|X|x))|(\\d{15})")
    //匹配
    val idMatcher: Matcher = idPattern.matcher(message)

    //查找手机号    可能会出现这种情况  11231213608861234987
    while(idMatcher.find()){
      //手机号的前一个 index，注：phoneMatcher.group()找到的手机号
      val beforeIndex = message.indexOf(idMatcher.group()) - 1
      //手机号的后一个 index
      val afterIndex = beforeIndex + idMatcher.group().length() + 1
      //手机号的前一个字符
      val beforeLetter = message.charAt(beforeIndex).toString()

      //如果前一位字符不是数字，那就要看后一位是否是数字
      if (!beforeLetter.matches("^[0-9]$")) {
        //判断后一位字符
        if (afterIndex < message.length()) {
          //手机号的后一个字符
          val afterLetter = message.charAt(afterIndex).toString()

          //后一位也不是数字，那说明这个字符串就是一个电话号码
          if (!afterLetter.matches("^[0-9]$")) {
            encrypted = encrypted.replace(idMatcher.group(), md5.getMD5ofStr(idMatcher.group()))
          }
        } else {
          encrypted = encrypted.replace(idMatcher.group(), md5.getMD5ofStr(idMatcher.group()))
        }
      }

    }

    //返回加密结果
    encrypted

  }
}
