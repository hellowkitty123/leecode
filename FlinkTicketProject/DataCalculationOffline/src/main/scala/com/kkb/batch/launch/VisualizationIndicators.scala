package com.kkb.batch.launch

import com.kkb.batch.function.{RequestFlag, SpiderFlag, UniqueFlag}
import com.kkb.batch.sql.SqlContent
import com.kkb.batch.tableSink.TableSinkUtil
import com.kkb.batch.utils.PropertiesUtil
import org.apache.flink.api.common.typeinfo.{BasicTypeInfo, TypeInformation, Types}
import org.apache.flink.api.java.io.jdbc.{JDBCAppendTableSink, JDBCAppendTableSinkBuilder}
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.table.api.Table
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.apache.flink.table.sources.CsvTableSource
import org.apache.flink.api.scala._
import org.apache.flink.types.Row

/**
  * 离线指标统计
  */
object VisualizationIndicators {

  def main(args: Array[String]): Unit = {

     //todo:1、构建批处理环境
      val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

     //todo:2、构建表的批处理环境
      val tableEnvironment: BatchTableEnvironment = BatchTableEnvironment.create(env)

     //获取数据路径--生产环境
     val defaultPathConfig = "offlineConfig.properties"
     val filePath = PropertiesUtil.getStringByKey("inputFilePath", defaultPathConfig)

     //todo:3、读取kafka原始日志数据
     val requestSource = CsvTableSource.builder().path("D:\\mock\\part-00000")
                                                 .field("requestMethod", Types.STRING)
                                                 .field("request", Types.STRING)
                                                 .field("remoteAddr", Types.STRING)
                                                 .field("httpUserAgent", Types.STRING)
                                                 .field("timeIso8601", Types.STRING)
                                                 .field("serverAddr", Types.STRING)
                                                 .field("highFrqIPGroup", Types.STRING)
                                                 .field("flightType", Types.STRING)
                                                 .field("behaviorType", Types.STRING)
                                                 .field("travelType",Types.STRING)
                                                 .field("flightDate",Types.STRING)
                                                 .field("depcity", Types.STRING)
                                                 .field("arrcity", Types.STRING)
                                                 .field("JSESSIONID", Types.STRING)
                                                 .field("USERID", Types.STRING)
                                                 .field("queryRequestDataStr", Types.STRING)
                                                 .field("bookRequestDataStr", Types.STRING)
                                                 .field("httpReferrer", Types.STRING)
                                                 .fieldDelimiter("#CS#") //字段的分隔符
                                                 .ignoreParseErrors() //忽略解析错误
                                                 .build()


    //todo:4、读取黑名单日志数据
    val ipBlockSource = CsvTableSource.builder().path("D:\\mock\\spiderIP.txt")
                                                .field("remoteAddr", Types.STRING)
                                                .field("flowID", Types.STRING)
                                                .field("score", Types.STRING)
                                                .field("ruleIDs", Types.STRING)
                                                .fieldDelimiter("|") //字段的分隔符
                                                .ignoreParseErrors() //忽略解析错误
                                                .build()



    //todo:5、注册成表
      //查询请求表
    tableEnvironment.registerTableSource("t_request", requestSource)
      //黑名单表
    tableEnvironment.registerTableSource("t_ipBlock", ipBlockSource)

    //todo: 6、表关联
     val spiderTable: Table = tableEnvironment.sqlQuery(SqlContent.spiderSQL)

    //todo: 7、注册自定义函数
     tableEnvironment.registerFunction("requestFlag",new RequestFlag)
     tableEnvironment.registerFunction("spiderFlag",new SpiderFlag)
     tableEnvironment.registerFunction("uniqueFlag",new UniqueFlag)

    //todo: 8、数据打标签
    val processTagTable: Table = spiderTable.select("request,requestFlag(request) as requestType,spiderFlag(flowID) as isSpider,travelType,flightType,substring(timeIso8601,0,11) as visitTime")
    tableEnvironment.registerTable("processTag",processTagTable)

    //todo:9、指标统计
      //todo: 9.1 国内查询转化率(查询--->旅客信息)
        //1、在processTagTable表中过滤出是国内的操作
        //2、在上面数据的基础上过滤出requestType=2/在上面数据的基础上过滤出requestType=1
          val natinalRateTable: Table = tableEnvironment.sqlQuery(SqlContent.natinalRateSQL)
          tableEnvironment.toDataSet[Row](natinalRateTable).print()

      //todo: 9.2 国际查询转化率(查询--->旅客信息)
        //1、在processTagTable表中过滤出是国际的操作
        //2、在上面数据的基础上过滤出requestType=2/在上面数据的基础上过滤出requestType=1
          val internatinalRateTable: Table = tableEnvironment.sqlQuery(SqlContent.internatinalSQL)
          tableEnvironment.toDataSet[Row](internatinalRateTable).print()


      //todo: 9.3 国内旅客转化率(旅客信息-支付)
        //1、在processTagTable表中过滤出是国内的操作
        //2、在上面数据的基础上过滤出requestType=3/在上面数据的基础上过滤出requestType=2
          val natinalTravellerRateTable: Table = tableEnvironment.sqlQuery(SqlContent.natinalTravellerRateSQL)
          tableEnvironment.toDataSet[Row](natinalTravellerRateTable).print()


      //todo: 9.4 国际旅客转化率(旅客信息-支付)
        //1、在processTagTable表中过滤出是国际的操作
        //2、在上面数据的基础上过滤出requestType=3/在上面数据的基础上过滤出requestType=2
          val internatinalTravellerRateTable: Table = tableEnvironment.sqlQuery(SqlContent.internatinalTravellerRateSQL)
          tableEnvironment.toDataSet[Row](internatinalTravellerRateTable).print()


      //todo: 9.5 爬虫用户转化率(航班选择--->旅客信息)    结果保存：nh_flight_query_conversion_rate表
        //1、在processTagTable表中筛选出是爬虫的数据
        //2、在上面数据的基础上过滤出requestType=2/在上面数据的基础上过滤出requestType=1
          val spiderQueryTravellerRateTable: Table = tableEnvironment.sqlQuery(SqlContent.spiderQueryTravellerRateSQL)
          tableEnvironment.toDataSet[Row](spiderQueryTravellerRateTable).print()

      //todo: 9.6 正常用户转化率(航班选择--->旅客信息)
        //1、在processTagTable表中筛选出是非爬虫的数据
        //2、在上面数据的基础上过滤出requestType=2/在上面数据的基础上过滤出requestType=1
          val nonspiderQueryTravellerRateTable: Table = tableEnvironment.sqlQuery(SqlContent.nonspiderQueryTravellerRateSQL)
          tableEnvironment.toDataSet[Row](nonspiderQueryTravellerRateTable).print()


      //todo: 9.7 全部用户转化率(航班选择-->旅客信息)
        //1、requestType=2/requestType=1
          val allTravellerRateTable: Table = tableEnvironment.sqlQuery(SqlContent.allTravellerRateSQL)
          tableEnvironment.toDataSet[Row](allTravellerRateTable).print()


//      //todo: 9.8 爬虫用户转化率(旅客信息-支付)
//        //1、在processTagTable表中筛选出是爬虫的数据
//        //2、requestType=3/requestType=2
//            val spiderTravellerPayRateTable: Table = tableEnvironment.sqlQuery(SqlContent.spiderTravellerPayRateSQL)
//            tableEnvironment.toDataSet[Row](spiderTravellerPayRateTable).print()
//
//      //todo: 9.9 正常用户转化率(旅客信息-支付)
//        //1、在processTagTable表中筛选出是非爬虫的数据
//        //2、requestType=3/requestType=2
//            val nonspiderTravellerPayRateTable: Table = tableEnvironment.sqlQuery(SqlContent.nonspiderTravellerPayRateSQL)
//            tableEnvironment.toDataSet[Row](nonspiderTravellerPayRateTable).print()
//
//
//      //todo: 9.10 全部用户转化率(旅客信息-支付)
//        //1、requestType=3/requestType=2
//            val allspiderTravellerPayRateTable: Table = tableEnvironment.sqlQuery(SqlContent.allspiderTravellerPayRateSQL)
//            tableEnvironment.toDataSet[Row](allspiderTravellerPayRateTable).print()
//
//
//       //todo: 9.11 国内单程、国内双程、国际单程、国际双程查询爬取频次
//          //1、针对每天的数据进行爬虫的过滤
//          //2、国内单程查询、国际单程查询、国内双程查询、国际双程查询：	先根据（flightType、travelType）分组，然后进行count操作
//            val spiderRateTable: Table = tableEnvironment.sqlQuery(SqlContent.spiderFrequencySQL)
//            tableEnvironment.toDataSet[Row](spiderRateTable).print()
//
//
//      //todo: 9.12 爬虫用户查定比(预定与查询的比值)
//          //1、针对每天的数据进行爬虫的过滤
//          //2、requestType=1/requestType=0
//            val spiderBookQueryRateTable: Table = tableEnvironment.sqlQuery(SqlContent.spiderBookQueryRateSQL)
//            tableEnvironment.toDataSet[Row](spiderBookQueryRateTable).print()
//
//
//      //todo: 9.13 正常用户查定比(预定与查询的比值)
//          //1、针对每天的数据进行爬虫的过滤
//          //2、requestType=1/requestType=0
//            val nonspiderBookQueryRateTable: Table = tableEnvironment.sqlQuery(SqlContent.nonspiderBookQueryRateSQL)
//            tableEnvironment.toDataSet[Row](nonspiderBookQueryRateTable).print()
//
//    /**
//      * 爬虫对系统稳定性的影响
//     */
//      //todo: 9.14 爬虫流量情况
//          val spiderFlowCountTable: Table = tableEnvironment.sqlQuery(SqlContent.spiderFlowCountSQL)
//          tableEnvironment.toDataSet[Row](spiderFlowCountTable).print()
//
//      //todo: 9.15 正常流量情况
//          val nonspiderFlowCountTable: Table = tableEnvironment.sqlQuery(SqlContent.nonspiderFlowCountSQL)
//          tableEnvironment.toDataSet[Row](nonspiderFlowCountTable).print()
//
//      //todo: 9.16 全部流量情况
//          val allFlowCountTable: Table = tableEnvironment.sqlQuery(SqlContent.allFlowCountSQL)
//          tableEnvironment.toDataSet[Row](allFlowCountTable).print()



     //todo: 10、table结果数据保存到mysql表中

        //todo: 10.1 国内国际查询转化率插入mysql表
          //定义国内国际查询转化率插入mysql的sql语句
            val  natinalAndInternatinalRateSQL="insert into nh_domestic_inter_conversion_rate(id,step_type,flight_type,conversion_rate,record_time) values(?,?,?,?,?)"
            val  natinalAndInternatinalTableSink = TableSinkUtil.getNatinalAndInternatinalTableSink(natinalAndInternatinalRateSQL)

        //定义表的schema信息
            val nifieldNames = Array("id","step_type","flight_type","conversion_rate","record_time")
            val natinalAndInternatinalRateSchema =  Array[TypeInformation[_]](Types.STRING, Types.INT, Types.INT, Types.FLOAT, Types.STRING)

        // 注册sink
         tableEnvironment.registerTableSink("natinalAndInternatinalJdbcSink",nifieldNames,natinalAndInternatinalRateSchema,natinalAndInternatinalTableSink)

        //数据写入到mysql表中
          //国内查询转化率
            natinalRateTable.insertInto("natinalAndInternatinalJdbcSink")
          //国际查询转化率
            internatinalRateTable.insertInto("natinalAndInternatinalJdbcSink")
          //国内旅客转化率(旅客信息-支付)
            natinalTravellerRateTable.insertInto("natinalAndInternatinalJdbcSink")
          //国际旅客转化率(旅客信息-支付)
            internatinalTravellerRateTable.insertInto("natinalAndInternatinalJdbcSink")


    //todo:10.2、爬虫用户转化率插入mysql的sql语句
      //定义爬虫用户转化率插入mysql的sql语句
        val  spiderQueryTravellerRateSQL="insert into nh_flight_query_conversion_rate(id,step_type,query_spider_type,conversion_rate,record_time) values(?,?,?,?,?)"
        val  spiderTableSink = TableSinkUtil.getNatinalAndInternatinalTableSink(spiderQueryTravellerRateSQL)
      //定义表的schema信息
        val spiderFieldNames = Array("id","step_type","query_spider_type","conversion_rate","record_time")
    val spiderRateSchema =  Array[TypeInformation[_]](Types.STRING, Types.INT, Types.INT, Types.FLOAT, Types.STRING)

      // 注册sink
        tableEnvironment.registerTableSink("spiderJdbcSink",spiderFieldNames,spiderRateSchema,spiderTableSink)

      //数据写入到mysql表中
        //爬虫用户转化率(航班选择--->旅客信息)
          spiderQueryTravellerRateTable.insertInto("spiderJdbcSink")
        //正常用户转化率(航班选择--->旅客信息)
          nonspiderQueryTravellerRateTable.insertInto("spiderJdbcSink")





    //启动执行
        tableEnvironment.execute("VisualizationIndicators")

  }
}
