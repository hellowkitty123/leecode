package com.kkb.batch.tableSink

import com.kkb.batch.utils.PropertiesUtil
import org.apache.flink.api.common.typeinfo.Types
import org.apache.flink.api.java.io.jdbc.{JDBCAppendTableSink, JDBCAppendTableSinkBuilder}


/**
  * 构建基于不同表存储的TableSink对象
  */
object TableSinkUtil {

  //mysql-url
  val url = PropertiesUtil.getStringByKey("url.path","mysqlConfig.properties")
  //driver.class
  val driver = PropertiesUtil.getStringByKey("driver.class","mysqlConfig.properties")
  //username
  val username = PropertiesUtil.getStringByKey("username","mysqlConfig.properties")
  //password
  val password = PropertiesUtil.getStringByKey("password","mysqlConfig.properties")
  //batch.size
  val batchSize = PropertiesUtil.getStringByKey("batch.size","mysqlConfig.properties").toInt




  /**
    * 构建转化率的TableSink对象
    * @param sql      插入的sql语句
    * @return
    */
    def  getNatinalAndInternatinalTableSink(sql:String): JDBCAppendTableSink ={
      val tableSink : JDBCAppendTableSink = new JDBCAppendTableSinkBuilder()
                                              .setDBUrl(url)
                                              .setDrivername(driver)
                                              .setUsername(username)
                                              .setPassword(password)
                                              .setBatchSize(batchSize)
                                              .setQuery(sql)
                                              .setParameterTypes(Types.STRING, Types.INT, Types.INT, Types.FLOAT, Types.STRING)
                                              .build()

      tableSink
    }




}
