package com.travel.programApp.hbaseSink

import com.travel.utils.HbaseTools
import org.apache.hadoop.hbase.client.Connection
import org.apache.spark.sql.Row
import org.apache.spark.sql.sources.v2.writer.{DataWriter, WriterCommitMessage}

class HBaseDataWriter(tableName:String,rowkey:String,hbaseFields:String) extends DataWriter[Row]{

  private val conn: Connection = HbaseTools.getHbaseConn

  override def write(record: Row): Unit = {
    val fields: Array[String] = hbaseFields.split(",")
    val myAsValue:Any = record.getAs(fields(0))
    val rowkeyStr: String = myAsValue.toString
    println(rowkeyStr)
    val map: Map[String, String] = record.getValuesMap(fields)
    HbaseTools.putMapData(conn,tableName,rowkeyStr,map)

  }

  override def commit(): WriterCommitMessage = {
    conn.close()
    null



  }

  override def abort(): Unit = {



  }



}
