package com.kkb.stream.dataprocess.mysqlsource

import java.sql.{Connection, PreparedStatement, ResultSet}
import java.util

import com.kkb.stream.common.bean.{FlowCollocation, RuleCollocation}
import com.kkb.stream.common.util.database.C3p0Util
import com.kkb.stream.common.util.jedis.JedisConnectionUtil
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import redis.clients.jedis.JedisCluster

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.control.Exception


/**
  * 自定义MysqlSource，读取处理规则数据
  */
class MysqlProcessRuleSource extends RichSourceFunction[util.HashMap[String,Any]]{
  var connection:Connection=_
  //定义关键页面查询的信息
    var ps1:PreparedStatement=_
    var rs1:ResultSet=_

  //定义高频ip查询的信息
    var ps2:PreparedStatement=_
    var rs2:ResultSet=_

  //定义流程规则策略配置的信息
    var ps3:PreparedStatement=_
    var rs3:ResultSet=_

    var map: util.HashMap[String, Any]=_

    //标识
    var isRunning=true


  /**
    * 初始化方法
    *
    * @param parameters
    */
  override def open(parameters: Configuration): Unit = {
    //获取数据库链接
    try{


    connection = C3p0Util.getConnection

    //todo:1、定义查询sql
      //todo: 1.1 关键页面的sql和结果
          val queryCriticalPagesSql = "select criticalPageMatchExpression from nh_query_critical_pages"
          ps1=connection.prepareStatement(queryCriticalPagesSql)
          rs1 = ps1.executeQuery()

      //todo: 1.2 高频ip的sql和结果
          val ipBlackRuleSql="select ip_name from nh_ip_blacklist"
          ps2=connection.prepareStatement(ipBlackRuleSql)
          rs2 = ps2.executeQuery()

      //todo: 1.3 流程规则策略配置的sql和结果
          val flowRuleSql="select nh_process_info.id," +
                          "nh_process_info.process_name," +
                          "nh_strategy.crawler_blacklist_thresholds " +
                          "from nh_process_info,nh_strategy " +
                          "where nh_process_info.id=nh_strategy.id and status=0"
          ps3=connection.prepareStatement(flowRuleSql)
          rs3 = ps3.executeQuery()



    //todo:2、定义map封装查询结果
      map= new util.HashMap[String, Any]()

        //todo:2.1 封装关键页面的数据
          map=queryCriticalPagesToMap(rs1,map)


        //todo: 2.2 封装高频ip规则的数据
         map=queryIpBlackRuleToMap(rs2,map)


        //todo: 2.3 封装流程规则策略配置的数据
        map=queryflowRuleToMap(rs3,map)
    }
  }


  /**
    * 读取mysql表数据
    * @param ctx
    */
  override def run(ctx: SourceFunction.SourceContext[util.HashMap[String, Any]]): Unit = {
    try {

      while(isRunning && !Thread.interrupted()) {
        //获取redis连接
        val jedisCluster: JedisCluster = JedisConnectionUtil.getJedisCluster
        //从redis中查询是否更新规则的标识
          //关键页面规则更新标识
            val criticalPagesIsUpdate = jedisCluster.get("criticalPagesChangeFlag").toBoolean

         //高频ip更新标识
            val ipBlackIsUpdate = jedisCluster.get("ipBlackRuleFlag").toBoolean

        //流程规则策略配置更新标识
            val flowIsUpdate = jedisCluster.get("flowRuleFlag").toBoolean

        if (criticalPagesIsUpdate) {

              println("---------[criticalPagesChangeFlag]规则发生变化----------")

            //移除map中的key
              map.remove("criticalPages")

            //查询mysql中更新的规则数据
              rs1 = ps1.executeQuery()

            //封装更新的规则到map中
              map=queryCriticalPagesToMap(rs1,map)

            //修改redis中的更新规则标识
              jedisCluster.set("criticalPagesChangeFlag", "false")

        }else if(ipBlackIsUpdate){

              println("---------[ipBlackRuleFlag]规则发生变化----------")

              //移除广播变量中的map
              map.remove("ipBlack")

              //查询mysql中更新的规则数据
              rs2 = ps2.executeQuery()

              //封装新的分类数据到map中
              map = queryIpBlackRuleToMap(rs2,map)

              //修改redis中的更新规则标识
              jedisCluster.set("ipBlackRuleFlag", "false")


        }else if(flowIsUpdate){
            println("---------[flowRuleFlag]规则发生变化----------")

            //移除广播变量中的map
            map.remove("flowRule")

            //查询mysql中更新的规则数据
            rs3 = ps3.executeQuery()

            //封装新的分类数据到map中
            map = queryflowRuleToMap(rs3,map)

            //修改redis中的更新规则标识
            jedisCluster.set("flowRuleFlag", "false")

        }

          //输出
          ctx.collect(map)

         //每隔5s检测redis中的key
        Thread.sleep(5000)
      }
    }catch {
      case e:Exception =>{
        Thread.interrupted()
      }
    }
  }

  /**
    * 取消发送数据
    */
  override def cancel(): Unit = {
    isRunning=false
  }


  /**
    * 查询关键页面规则数据，封装成map
    * @param rs
    * @return
    */
  def queryCriticalPagesToMap(rs: ResultSet,map:util.HashMap[String, Any]):util.HashMap[String,Any] ={

    val criticalPagesArray = ArrayBuffer[String]()
    //遍历过滤规则数据
    while (rs.next()) {
      val value: String = rs.getString("criticalPageMatchExpression")
      //添加过滤规则到list集合中
      criticalPagesArray.+=(value)
      //添加key-value到map中，这里用得到固定的key，方便后续删除
      map.put("criticalPages" , criticalPagesArray)
    }

    map
  }

  /**
    * 查询流程规则策略配置数据，封装成map
    * @param rs
    * @return
    */
  def queryflowRuleToMap(rs: ResultSet, map: util.HashMap[String, Any]): util.HashMap[String, Any] ={

    val flowRuleArray = ArrayBuffer[FlowCollocation]()

    //遍历
    while (rs.next()) {
      val flowId = rs.getString("id")
      val flowName = rs.getString("process_name")
      val flowLimitScore = rs.getDouble("crawler_blacklist_thresholds")
      //获取流程列表，封装数据
      flowRuleArray += new FlowCollocation(flowId, flowName, createRuleList(flowId), flowLimitScore, flowId)

    }

    //封装流程规则策略配置数据到map中
    map.put("flowRule",flowRuleArray)

    //返回map
    map
  }


  /**
    * 查询高频ip数据，封装成map
    * @param rs
    * @param map
    * @return
    */
  def queryIpBlackRuleToMap(rs: ResultSet, map: util.HashMap[String, Any]): util.HashMap[String,Any] ={
    //存储高频ip数据
    val ipBlackList =  ArrayBuffer[String]()

    //遍历
    while(rs.next()){
      val ip: String = rs.getString("ip_name")
      ipBlackList.+=(ip)

    }
    //封装数据到map中
    map.put("ipBlack",ipBlackList)

    //返回map
    map

  }


  /**
    * 获取规则列表
    *
    * @param processId 根据该ID 查询规则
    * @return list 列表
    */
  def createRuleList(processId:String):List[RuleCollocation] = {
    var list = new ListBuffer[RuleCollocation]
     //查询对应流程的规则
    val sql = "select * from(" +
                  "select " +
                    "nh_rule.id," +
                    "nh_rule.process_id," +
                    "nh_rules_maintenance_table.rule_real_name," +
                    "nh_rule.rule_type,nh_rule.crawler_type,"+
                    "nh_rule.status,nh_rule.arg0," +
                    "nh_rule.arg1," +
                    "nh_rule.score " +
                  "from nh_rule,nh_rules_maintenance_table where nh_rules_maintenance_table.rule_name=nh_rule.rule_name) " +
              "as tab where process_id = '"+ processId + "'and crawler_type=0"

        val ps: PreparedStatement = connection.prepareStatement(sql)
        val rs: ResultSet = ps.executeQuery()
        while ( rs.next() ) {
          val ruleId = rs.getString("id")
          val flowId = rs.getString("process_id")
          val ruleName = rs.getString("rule_real_name")
          val ruleType = rs.getString("rule_type")
          val ruleStatus = rs.getInt("status")
          val ruleCrawlerType = rs.getInt("crawler_type")
          val ruleValue0 = rs.getDouble("arg0")
          val ruleValue1 = rs.getDouble("arg1")
          val ruleScore = rs.getInt("score")
          val ruleCollocation = new RuleCollocation(ruleId,
                                                    flowId,
                                                    ruleName,
                                                    ruleType,
                                                    ruleStatus,
                                                    ruleCrawlerType,
                                                    ruleValue0,
                                                    ruleValue1,
                                                    ruleScore)

          list += ruleCollocation
      }

     list.toList
  }


}

