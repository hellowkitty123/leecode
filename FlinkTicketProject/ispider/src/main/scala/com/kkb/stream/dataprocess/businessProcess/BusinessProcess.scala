package com.kkb.stream.dataprocess.businessProcess

import java.net.InetSocketAddress
import java.util
import java.util.UUID
import com.kkb.stream.common.util.jedis.PropertiesUtil
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisClusterConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}
import org.codehaus.jackson.map.ObjectMapper
/**
  * 该类中实现大量的业务处理
  */
object BusinessProcess {

  /**
    * 统计链路的功能方法
    * @param sourceData
    */
  def  linkCount(sourceData:DataStream[String]): Unit ={

     //todo:1、统计链路流量
    val visitServerCount: DataStream[(String, Int)] = sourceData.map(record => {
      //对消息的进行分割  #CS#
      val value: Array[String] = record.split("#CS#")

      //封装数据为tuple   （server_addr,1）
      if (value.length > 9) (value(9), 1) else ("", 1)
    }).keyBy(0).timeWindow(Time.seconds(5)).sum(1)

    visitServerCount.print("统计链路流量")

   //todo:2、统计nginx服务器的活跃连接数（server_addr，连接数）
    val activeCount: DataStream[(String, Int)] = sourceData.map(record => {
      //对消息的进行分割  #CS#
      val value: Array[String] = record.split("#CS#")

      //封装数据为tuple   （server_addr,连接数）
      if (value.length > 11) (value(9), value(11).toInt) else ("", 1)
    }).keyBy(0).timeWindow(Time.seconds(5)).reduce((x, y) => y)

    activeCount.print("活跃连接数")

    /**
      * 保存结果到redis中   flink-connector-redis
      */
    //获取FlinkJedisClusterConfig配置对象
    val jedisClusterConfig: FlinkJedisClusterConfig = getFlinkJedisClusterConfig()

    //获取redis  sink 保存统计链路流量
    val redisSink1 = new RedisSink[Tuple2[String,Int]](jedisClusterConfig,new MyRedisMapper1)

    //使用我们自定义的sink
    visitServerCount.addSink(redisSink1)

    //获取redis  sink 统计nginx服务器的活跃连接数
    val redisSink2 = new RedisSink[Tuple2[String,Int]](jedisClusterConfig,new MyRedisMapper2)

    //使用我们自定义的sink
    activeCount.addSink(redisSink2)

  }


  /**
    * 获取FlinkJedisClusterConfig配置对象
    * @return
    */
  def getFlinkJedisClusterConfig():FlinkJedisClusterConfig={
    //redis集群模式
    val builder = new FlinkJedisClusterConfig.Builder
    val addresses = new util.HashSet[InetSocketAddress]()
    //获取redis集群地址
    // redis.servers = 192.168.200.100:7001,192.168.200.100:7002,192.168.200.100:7003,192.168.200.100:7004,192.168.200.100:7005,192.168.200.100:7006
    val redisServer: String = PropertiesUtil.getStringByKey("redis.servers","jedisConfig.properties")

    //切分，构建InetSocketAddress对象
    val serverArray: Array[String] = redisServer.split(",")
    for(hostPort <- serverArray){
      val server: Array[String] = hostPort.split(":")
      addresses.add(new InetSocketAddress(server(0),server(1).toInt))
    }

    //设置redis集群地址
    builder.setNodes(addresses )
    //设置连接池最大连接数
    builder.setMaxIdle(PropertiesUtil.getStringByKey("maxTotal","jedisConfig.properties").toInt)
    //设置最大空闲连接数
    builder.setMaxIdle(PropertiesUtil.getStringByKey("maxIdle","jedisConfig.properties").toInt)
    //设置最小空闲连接数
    builder.setMaxIdle(PropertiesUtil.getStringByKey("minIdle","jedisConfig.properties").toInt)
    //设置超时时间
    builder.setTimeout(PropertiesUtil.getStringByKey("connectionTimeout","jedisConfig.properties").toInt)
    //设置重试次数
    builder.setMaxRedirections(PropertiesUtil.getStringByKey("maxAttempts","jedisConfig.properties").toInt)

    //构建FlinkJedisClusterConfig配置对象
    val jedisClusterConfig: FlinkJedisClusterConfig = builder.build()
    jedisClusterConfig
  }
}

/**
  * 保存服务器访问流量到redis中
  */
class MyRedisMapper1  extends RedisMapper[Tuple2[String,Int]]{

  override def getCommandDescription: RedisCommandDescription = {
    //获取redis key的过期时间 默认是24小时
    val ttl: Int = PropertiesUtil.getStringByKey("cluster.exptime.monitor","jedisConfig.properties").toInt

    //设置插入数据到redis的命令，带上key的过期时间
    new RedisCommandDescription(RedisCommand.SET)

  }
  //指定key
  override def getKeyFromData(data: (String, Int)): String = {
    "CSANTI_MONITOR_LP"+System.currentTimeMillis()+UUID.randomUUID().toString

  }

  //指定value
  override def getValueFromData(data: (String, Int)): String = {
      //定义ObjectMapper对象，实现map转换为json
     val mapper = new ObjectMapper()
     val map1 = new util.HashMap[String,Int]()
      map1.put(data._1,data._2)
      //再定义一个map封装数据
      val map2 = new util.HashMap[String,util.HashMap[String,Int]]()

      map2.put("serversCountMap",map1)    //{"serversCountMap":{"192.168.200.100":60}}
     val jsonString: String = mapper.writeValueAsString(map2)
     println(jsonString)

    jsonString

  }

}


/**
  * 保存当前活跃连接数到redis的sink
  */
class MyRedisMapper2  extends RedisMapper[Tuple2[String,Int]]{

  override def getCommandDescription: RedisCommandDescription = {
    //获取redis key的过期时间 默认是24小时
    val ttl: Int = PropertiesUtil.getStringByKey("cluster.exptime.monitor","jedisConfig.properties").toInt

    //设置插入数据到redis的命令，带上key的过期时间
    new RedisCommandDescription(RedisCommand.SET)

  }
  //指定key
  override def getKeyFromData(data: (String, Int)): String = {
    "CSANTI_MONITOR_AP"+System.currentTimeMillis()+UUID.randomUUID().toString

  }

  //指定value
  override def getValueFromData(data: (String, Int)): String = {
    //定义ObjectMapper对象，实现map转换为json
    val mapper = new ObjectMapper()
    val map1 = new util.HashMap[String,Int]()
    map1.put(data._1,data._2)
    //再定义一个map封装数据
    val map2 = new util.HashMap[String,util.HashMap[String,Int]]()

    map2.put("activeNumMap",map1)    //{"activeNumMap":{"192.168.200.100":33}}
    val jsonString: String = mapper.writeValueAsString(map2)
    println(jsonString)

    jsonString

  }



}

