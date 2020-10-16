package com.kkb.batch.bean

//用户请求数据
case class Requests(requestMethod: String, request: String, remoteAddr: String, httpUserAgent: String, timeIso8601: String, serverAddr: String, criticalCookie: String, highFrqIPGroup: String, flightType: String, behaviorType: String, travelType: String, flightDate: String, depcity: String, arrcity: String, JSESSIONID: String, USERID: String, queryRequestDataStr: String, bookRequestDataStr: String, httpReferrer: String, StageTag: String, Spider: Int)
