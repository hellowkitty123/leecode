package com.travel.programApp

import java.util
import java.util.Optional

import com.travel.utils.HbaseTools
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{Connection, Put, ResultScanner, Scan, Table}
import org.apache.spark.sql.sources.v2.reader.{DataReader, DataReaderFactory, DataSourceReader}
import org.apache.spark.sql.sources.v2.writer.{DataSourceWriter, DataWriter, DataWriterFactory, WriterCommitMessage}
import org.apache.spark.sql.sources.v2.{DataSourceOptions, DataSourceV2, ReadSupport, WriteSupport}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}

object HbaseSourceAndSink {
  def main(args: Array[String]): Unit = {
    val session = SparkSession.builder().master("local[4]").appName("diysinkandsoure").getOrCreate()
    session.sparkContext.setLogLevel("WARN")
    val frame = session.read.format("com.travel.programApp.HBaseSource")
      .option("hbase.table.name", "spark_hbase_sql")
      .option("cf.cc", "cf:name,cf:score")
      .option("schema", "`name` STRING,`score` STRING")
      .load()
    frame.printSchema()
    frame.explain(true)
    frame.show()
    frame.createOrReplaceTempView("sparkHbaseSql")
    val frame2: DataFrame = session.sql(" select * from sparkHbaseSql where score > 60")
    val frame1 = frame2
    frame1.show()

    frame.write.format("com.travel.programApp.HBaseSink")
      .mode(SaveMode.Overwrite)
      .option("hbase.table.name","final_summary_pat_order")
      .option("hbase.rowkey","rk")
      .option("hbase.fields","rk,city_name,_year_comple_rate,_quarter_comple_rate,_month_comple_rate,_week_comple_rate,_day_comple_rate")
      .save()
  }

}

class HBaseSink extends DataSourceV2 with WriteSupport {
  override def createWriter(s: String, structType: StructType, saveMode: SaveMode, dataSourceOptions: DataSourceOptions): Optional[DataSourceWriter] = {
    val tableName = dataSourceOptions.get("hbase.table.name").get()
    Optional.of(new HBaseDataSourceWriter(tableName))
  }
}
class HBaseDataSourceWriter(tableName : String) extends DataSourceWriter{
  override def createWriterFactory(): DataWriterFactory[Row] = {
    new HBaseDataWriterFactory(tableName)
  }

  override def commit(writerCommitMessages: Array[WriterCommitMessage]): Unit = {

  }

  override def abort(writerCommitMessages: Array[WriterCommitMessage]): Unit = {

  }
}

class HBaseDataWriterFactory(tableName: String) extends DataWriterFactory[Row]{
  override def createDataWriter(i: Int, i1: Int): DataWriter[Row] = {
    new HBaseDataWriter(tableName)
  }
}

class HBaseDataWriter(tableName: String) extends DataWriter[Row]{
  private val conn: Connection = HbaseTools.getHbaseConn
  private val table: Table = conn.getTable(TableName.valueOf(tableName))
  override def write(record: Row): Unit = {
    val name = record.getString(0)
    val score = record.getString(1)
    val put = new Put("001".getBytes())
    put.addColumn("cf".getBytes(),"name".getBytes(),name.getBytes())
    put.addColumn("cf".getBytes(),"score".getBytes(),score.getBytes())
    table.put(put)

  }

  override def commit(): WriterCommitMessage = {
    table.close()
    conn.close()
    null
  }

  override def abort(): Unit = {

  }
}

class HBaseSource extends DataSourceV2 with ReadSupport {
  override def createReader(dataSourceOptions: DataSourceOptions): DataSourceReader = {
    val tableName = dataSourceOptions.get("hbase.table.name").get()
    val cfAndCC = dataSourceOptions.get("cf.cc").get()
    val schema = dataSourceOptions.get("schema").get()
    val reader: HBaseDataSourceReader = new HBaseDataSourceReader(tableName, cfAndCC, schema)
    reader
  }
}

class HBaseDataSourceReader(tableName:String,cfAndCC:String,schema:String) extends DataSourceReader{
  private val structType: StructType = StructType.fromDDL(schema)
  override def readSchema(): StructType = {
    structType
  }


  override def createDataReaderFactories(): util.List[DataReaderFactory[Row]] = {
    import scala.collection.JavaConverters._
    Seq(
      new HbaseDataReaderFactory(tableName,cfAndCC).asInstanceOf[DataReaderFactory[Row]]
    ).asJava
  }
}

class HbaseDataReaderFactory(tableName:String,cfAndCC:String) extends DataReaderFactory[Row]{
  override def createDataReader(): DataReader[Row] = {
    new HBaseDataReader(tableName,cfAndCC)
  }

}

class HBaseDataReader(tableName:String,cfAndCC:String) extends DataReader[Row]{

  val data:Iterator[Seq[AnyRef]] = getIterator
  var resultScanner: ResultScanner = _

  def getIterator: Iterator[Seq[AnyRef]] = {
    import scala.collection.JavaConverters._
    val conn = HbaseTools.getHbaseConn
    val table = conn.getTable(TableName.valueOf( tableName))
    resultScanner = table.getScanner(new Scan())
    val iterator = resultScanner.iterator().asScala.map(eachResult => {
      val name = eachResult.getValue("cf".getBytes(), "name".getBytes())
      val age = eachResult.getValue("cf".getBytes(), "age".getBytes())
      Seq(name, age)
    })
    conn.close()
    iterator
  }

  override def next(): Boolean = {
    data.hasNext
  }

  override def get(): Row = {
    val seq: Seq[AnyRef] = data.next()
    val row: Row = Row.fromSeq(seq)
    row
  }

  override def close(): Unit = {

  }
}




