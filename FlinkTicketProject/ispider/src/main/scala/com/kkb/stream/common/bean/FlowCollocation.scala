package com.kkb.stream.common.bean

/**
 * 流程类：对应表nh_process_info
 * （规则配置集合和阈值参数）
 */
case class FlowCollocation(flowId: String, flowName: String, rules: List[RuleCollocation], flowLimitScore: Double = 100,strategyCode:String)

/**
  * 封装流程计算结果
  *
  * @param flowId 流程id
  * @param flowScore 流程得分
  * @param flowLimitedScore 流程阈值
  * @param isUpLimited 流程得分是否大于阈值
  * @param flowStrategyCode 流程策略代码
*/
case class FlowScoreResult(flowId: String,
                           flowScore: Double,
                           flowLimitedScore: Double,
                           isUpLimited: Boolean,
                           flowStrategyCode: String,
                           hitRules: List[String],
                           hitTime: String)


/**
  * 反爬计算结果封装类
  * @param ip 请求IP
  * @param ipAccessCounts 单位时间内某个IP访问量
  * @param criticalPageAccessCounts 单位时间内的关键页面访问总量
  * @param userAgentCounts 单位时间内的UA种类数统计
  * @param critivalPageMinInterval 单位时间内的关键页面最短访问间隔
  * @param accessPageIntervalLessThanDefault 单位时间内小于最短访问间隔（自设）的关键页面查询次数
  * @param differentTripQuerysCounts 单位时间内查询不同行程的次数
  * @param criticalCookies 单位时间内关键页面的Cookie数
  * @param flowsScore 流程计算结果
  */
case class AntiCalculateResult(ip: String,
                               ipAccessCounts: Int,
                               criticalPageAccessCounts: Int,
                               userAgentCounts: Int,
                               critivalPageMinInterval: Int,
                               accessPageIntervalLessThanDefault: Int,
                               differentTripQuerysCounts: Int,
                               criticalCookies: Int,
                               flowsScore: Array[FlowScoreResult])
