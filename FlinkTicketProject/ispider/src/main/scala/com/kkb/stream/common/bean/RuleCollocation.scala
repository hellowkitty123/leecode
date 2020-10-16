package com.kkb.stream.common.bean

/**
  * 规则配置：对应表nh_rule
  * 保存各规则参数配置
  */
case class RuleCollocation(ruleId: String, //规则id
                           flowId: String, //流程id
                           ruleName: String, //规则名
                           ruleType: String, //规则类型
                           ruleStatus: Int, //规则状态
                           ruleCrawlerType: Int, //规则爬虫类型
                           ruleValue0: Double, //参数
                           ruleValue1: Double, //参数
                           ruleScore: Int //打分
                          ) {


}
