package com.kkb.stream.dataprocess.mysqlsource

import java.sql.{Connection, PreparedStatement, ResultSet}
import java.util

import com.kkb.stream.common.bean.AnalyzeRule
import com.kkb.stream.common.util.database.{C3p0Test, C3p0Util}
import com.kkb.stream.common.util.jedis.JedisConnectionUtil
import com.kkb.stream.dataprocess.constants.{BehaviorTypeEnum, FlightTypeEnum}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import redis.clients.jedis.JedisCluster
import org.apache.flink.api.scala._

import scala.collection.mutable.ArrayBuffer


/**
  * 自定义MysqlSource，读取规则数据
  */
class MysqlRuleSource extends RichSourceFunction[util.HashMap[String,Any]]{
  var connection:Connection=_
  //定义过滤规则查询的对象
    var ps1:PreparedStatement=_
    var rs1:ResultSet=_

  //定义分类规则查询的信息
    var ps2:PreparedStatement=_
    var rs2:ResultSet=_


  //定义解析规则的信息
    var ps3:PreparedStatement=_
    var rs3:ResultSet=_


  //定义高频ip查询的信息
    var ps4:PreparedStatement=_
    var rs4:ResultSet=_

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
    connection = C3p0Util.getConnection
//    connection = C3p0Test.getConnection
    //todo:1、定义查询sql
      //todo: 1.1 过滤规则的sql和结果
          val filterRuleSql="select id,value from nh_filter_rule"
          ps1=connection.prepareStatement(filterRuleSql)
          rs1 = ps1.executeQuery()

      //todo: 1.2 定义查询分类规则的sql和结果
          val classifyRuleSql="select id,expression,flight_type,operation_type from nh_classify_rule"
          ps2=connection.prepareStatement(classifyRuleSql)
          rs2 = ps2.executeQuery()

     //todo: 1.3 定义查询解析规则的sql和结果
          val analyzeruleRuleSql="select * from analyzerule"
          ps3=connection.prepareStatement(analyzeruleRuleSql)
          rs3 = ps3.executeQuery()

     //todo: 1.4 定义查询高频ip规则的sql和结果
          val ipBlackRuleSql="select ip_name from nh_ip_blacklist"
          ps4=connection.prepareStatement(ipBlackRuleSql)
          rs4 = ps4.executeQuery()



    //todo:2、定义map封装查询结果
      map= new util.HashMap[String, Any]()

        //todo:2.1 封装过滤规则的数据
          map=queryFilterRuleToMap(rs1,map)


        //todo: 2.2 封装分类规则的数据
         map=queryClassifyRuleToMap(rs2,map)


        //todo: 2.3 封装解析规则的数据
         map=queryAnalyzeruleRuleToMap(rs3,map)


        //todo: 2.4 封装高频ip规则的数据
        map=queryIpBlackRuleToMap(rs4,map)

  }


  /**
    * 读取mysql表数据
    * @param ctx
    */
  override def run(ctx: SourceFunction.SourceContext[util.HashMap[String, Any]]): Unit = {
    try {

      while(isRunning && ! Thread.interrupted()) {
        //获取redis连接
        val jedisCluster: JedisCluster = JedisConnectionUtil.getJedisCluster
        //从redis中查询是否更新规则的标识
          //过滤规则标识
            val filterIsUpdate = jedisCluster.get("filterChangeFlag").toBoolean

          //分类规则标识
             val classifyIsUpdate = jedisCluster.get("classifyRuleFlag").toBoolean

         //解析规则标识
              val analyzeIsUpdate = jedisCluster.get("analyzeRuleFlag").toBoolean

         //高频ip标识
              val ipBlackIsUpdate = jedisCluster.get("ipBlackRuleFlag").toBoolean

        if (filterIsUpdate) {

              println("---------[filterChangeFlag]规则发生变化----------")

            //移除map中的key
              map.remove("filterRule")

            //查询mysql中更新的规则数据
              rs1 = ps1.executeQuery()

            //封装更新的规则到map中
              map=queryFilterRuleToMap(rs1,map)

            //修改redis中的更新规则标识
              jedisCluster.set("filterChangeFlag", "false")

        } else if(classifyIsUpdate){

            println("---------[classifyRuleFlag]规则发生变化----------")

           //移除广播变量中的map
            map.remove("nationQuery")
            map.remove("internationQuery")
            map.remove("nationBook")
            map.remove("internationBook")

          //查询mysql中更新的规则数据
            rs2 = ps2.executeQuery()

          //封装新的分类数据到map中
            map = queryClassifyRuleToMap(rs2,map)

          //修改redis中的更新规则标识
            jedisCluster.set("classifyRuleFlag", "false")

        }else if(analyzeIsUpdate){

            println("---------[analyzeRuleFlag]规则发生变化----------")

            //移除广播变量中的map
              map.remove("query")
              map.remove("book")

            //查询mysql中更新的规则数据
              rs3 = ps3.executeQuery()

            //封装新的分类数据到map中
              map = queryClassifyRuleToMap(rs3,map)

            //修改redis中的更新规则标识
              jedisCluster.set("analyzeRuleFlag", "false")

        }else if(ipBlackIsUpdate){

              println("---------[ipBlackRuleFlag]规则发生变化----------")

              //移除广播变量中的map
              map.remove("ipBlack")

              //查询mysql中更新的规则数据
              rs4 = ps4.executeQuery()

              //封装新的分类数据到map中
              map = queryClassifyRuleToMap(rs4,map)

              //修改redis中的更新规则标识
              jedisCluster.set("ipBlackRuleFlag", "false")


        }

          //输出
          ctx.collect(map)

         //每隔5s检测redis中的key
        Thread.sleep(5000)
      }
    }catch {
      case e:Exception =>e.printStackTrace()
    }
  }

  /**
    * 取消发送数据
    */
  override def cancel(): Unit = {
    isRunning=false
  }


  /**
    * 查询过滤规则数据，封装成map
    * @param rs
    * @return
    */
  def queryFilterRuleToMap(rs: ResultSet,map:util.HashMap[String, Any]):util.HashMap[String, Any] ={


    val filterList = ArrayBuffer[String]()
    //遍历过滤规则数据
    while (rs.next()) {
      val value: String = rs.getString("value")
      //添加过滤规则到list集合中
      filterList.+=(value)
      //添加key-value到map中，这里用得到固定的key，方便后续删除
      map.put("filterRule" , filterList)
    }

    map
  }

  /**
    * 查询分类规则数据，封装成map
    * @param rs
    * @return
    */
  def queryClassifyRuleToMap(rs: ResultSet, map: util.HashMap[String, Any]): util.HashMap[String, Any] ={
    //国内查询
    val nationQueryList = ArrayBuffer[String]()
    //国际查询
    val internationQueryList = ArrayBuffer[String]()
    //国内预定
    val nationBookList = ArrayBuffer[String]()
    //国际预定
    val internationBookList = ArrayBuffer[String]()

    //遍历
    while (rs.next()) {
      //国内查询(0,0)，国际查询(1,0)，国内预定(0,1)，国际预定(1,1)

      val id: String = rs.getString("id")
      val flight_type: Int = rs.getInt("flight_type")
      val operation_type: Int = rs.getInt("operation_type")
      val value: String = rs.getString("expression")

      if(flight_type == FlightTypeEnum.National.id && operation_type == BehaviorTypeEnum.Query.id){
        //国内查询(0,0)
        nationQueryList.+=(value)

      }

      if(flight_type == FlightTypeEnum.International.id && operation_type == BehaviorTypeEnum.Query.id){
        //国际查询(1,0)
        internationQueryList.+=(value)

      }
      if(flight_type == FlightTypeEnum.National.id && operation_type == BehaviorTypeEnum.Book.id){
        //国内预定(0,1)
        nationBookList.+=(value)

      }
      if(flight_type == FlightTypeEnum.International.id && operation_type == BehaviorTypeEnum.Book.id){
        //国际预定(1,1)
        internationBookList.+=(value)

      }

    }

    //封装分类数据到map中
    map.put("nationQuery",nationQueryList)
    map.put("internationQuery",internationQueryList)
    map.put("nationBook",nationBookList)
    map.put("internationBook",internationBookList)

    //返回map
    map
  }


  /**
    * 查询解析规则数据，封装成map
    * @param rs
    * @param map
    * @return
    */
  def queryAnalyzeruleRuleToMap(rs: ResultSet, map: util.HashMap[String, Any]): util.HashMap[String, Any] ={
    //存储解析查询规则的数据
      val queryRuleList =  ArrayBuffer[AnalyzeRule]()

    //存储解析预定规则的数据
      val bookRuleList =  ArrayBuffer[AnalyzeRule]()

    //遍历
    while(rs.next()){
      val analyzeRule = new AnalyzeRule()
      val behavior_type: Int = rs.getString("behavior_type").toInt
      analyzeRule.id = rs.getString("id")
      analyzeRule.flightType = rs.getString("flight_type").toInt
      analyzeRule.BehaviorType = rs.getString("behavior_type").toInt
      analyzeRule.requestMatchExpression = rs.getString("requestMatchExpression")
      analyzeRule.requestMethod = rs.getString("requestMethod")
      analyzeRule.isNormalGet = rs.getString("isNormalGet").toBoolean
      analyzeRule.isNormalForm = rs.getString("isNormalForm").toBoolean
      analyzeRule.isApplicationJson = rs.getString("isApplicationJson").toBoolean
      analyzeRule.isTextXml = rs.getString("isTextXml").toBoolean
      analyzeRule.isJson = rs.getString("isJson").toBoolean
      analyzeRule.isXML = rs.getString("isXML").toBoolean
      analyzeRule.formDataField = rs.getString("formDataField")
      analyzeRule.book_bookUserId = rs.getString("book_bookUserId")
      analyzeRule.book_bookUnUserId = rs.getString("book_bookUnUserId")
      analyzeRule.book_psgName = rs.getString("book_psgName")
      analyzeRule.book_psgType = rs.getString("book_psgType")
      analyzeRule.book_idType = rs.getString("book_idType")
      analyzeRule.book_idCard = rs.getString("book_idCard")
      analyzeRule.book_contractName = rs.getString("book_contractName")
      analyzeRule.book_contractPhone = rs.getString("book_contractPhone")
      analyzeRule.book_depCity = rs.getString("book_depCity")
      analyzeRule.book_arrCity = rs.getString("book_arrCity")
      analyzeRule.book_flightDate = rs.getString("book_flightDate")
      analyzeRule.book_cabin = rs.getString("book_cabin")
      analyzeRule.book_flightNo = rs.getString("book_flightNo")
      analyzeRule.query_depCity = rs.getString("query_depCity")
      analyzeRule.query_arrCity = rs.getString("query_arrCity")
      analyzeRule.query_flightDate = rs.getString("query_flightDate")
      analyzeRule.query_adultNum = rs.getString("query_adultNum")
      analyzeRule.query_childNum = rs.getString("query_childNum")
      analyzeRule.query_infantNum = rs.getString("query_infantNum")
      analyzeRule.query_country = rs.getString("query_country")
      analyzeRule.query_travelType = rs.getString("query_travelType")
      analyzeRule.book_psgFirName = rs.getString("book_psgFirName")
      //查询
      if(behavior_type == 0){
        queryRuleList.+=(analyzeRule)
        //预定
      }else if(behavior_type == 1){
        bookRuleList.+=(analyzeRule)
      }
    }

    //查询规则
    map.put("query",queryRuleList)
    //预定规则
    map.put("book",bookRuleList)


    //返回map
    map

  }


  /**
    * 查询高频ip数据，封装成map
    * @param rs
    * @param map
    * @return
    */
  def queryIpBlackRuleToMap(rs: ResultSet, map: util.HashMap[String, Any]): util.HashMap[String, Any] ={
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

}