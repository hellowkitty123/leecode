package com.kkb.stream.dataprocess.broadcast

import java.util

import com.kkb.stream.common.bean._
import com.kkb.stream.dataprocess.businessProcess._
import com.kkb.stream.dataprocess.constants.{BehaviorTypeEnum, FlightTypeEnum, TravelTypeEnum}
import org.apache.flink.api.common.state.{BroadcastState, MapStateDescriptor, ReadOnlyBroadcastState}
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.util.Collector

import scala.collection.mutable.ArrayBuffer

/**
  * 自定义BroadcastProcessFunction
  */
class ProcessRuleBroadcastProcessFunction extends BroadcastProcessFunction[ProcessedData,util.HashMap[String,Any],(ProcessedData,util.HashMap[String, Any])]{

  //定义MapStateDescriptor
  val mapStateDesc = new MapStateDescriptor("processRule",classOf[Void],classOf[util.HashMap[String,Any]])


  /**
    * Broadcast State在processBroadcastElement方法中进行修改
    * @param map 广播流的数据
    * @param ctx 上下文
    * @param out 输出数据
    */
  override def processBroadcastElement(map: util.HashMap[String, Any], ctx: BroadcastProcessFunction[ProcessedData, util.HashMap[String, Any], (ProcessedData,util.HashMap[String, Any])]#Context, out: Collector[(ProcessedData,util.HashMap[String, Any])]): Unit ={
    //获取广播状态
    val ruleBroadCastState: BroadcastState[Void, util.HashMap[String, Any]] = ctx.getBroadcastState(mapStateDesc)

    //清理状态
    ruleBroadCastState.clear()

    //更新状态
    ruleBroadCastState.put(null,map)
  }

  /**
    * 读取规则，并基于规则，处理事件流中的数据
    * @param message  事件流中的数据
    * @param ctx    上下文
    * @param out    输出数据
    */
  override def processElement(message: ProcessedData, ctx: BroadcastProcessFunction[ProcessedData, util.HashMap[String, Any], (ProcessedData,util.HashMap[String, Any])]#ReadOnlyContext, out: Collector[(ProcessedData,util.HashMap[String, Any])]): Unit ={
    //todo: 1、获取广播状态
        val ruleBroadCastState: ReadOnlyBroadcastState[Void, util.HashMap[String, Any]] = ctx.getBroadcastState(mapStateDesc)
        val ruleDataMap:  util.HashMap[String, Any] = ruleBroadCastState.get(null)

    //todo:2、输出
        out.collect((message,ruleDataMap))

    }


}
