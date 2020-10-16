package com.kkb.batch.sql

/**
  * 开发离线指标统计的sql语句
  */
object SqlContent {

      //todo:1、标记爬虫的sql
      val spiderSQL=
        """
          |select
          |  request,
          |  flowID,
          |  travelType,
          |  flightType,
          |  timeIso8601
          |from t_request
          |	left join t_ipBlock ON t_request.remoteAddr = t_ipBlock.remoteAddr
        """.stripMargin


      //todo:2、国内查询转化率(查询--->旅客信息)      结果存储mysql的表: nh_domestic_inter_conversion_rate
        val natinalRateSQL=
          """
            |select
            |    uniqueFlag(),
            |    0,
            |    0,
            |   sum(case when requestType = '2' then 1 else 0 end)/cast(sum(case when requestType = '1' then 1 else 0 end) as FLOAT),
            |   max(visitTime)
            |from processTag
            |where flightType = 'National'
          """.stripMargin


    //todo:3、国际查询转化率(查询--->旅客信息)
    val internatinalSQL=
      """
        |select
        |    uniqueFlag(),
        |    0,
        |    1,
        |   sum(case when requestType = '2' then 1 else 0 end)/cast(sum(case when requestType = '1' then 1 else 0 end) as FLOAT),
        |   max(visitTime)
        |from processTag
        |where flightType = 'Internatinal'
      """.stripMargin


    //todo:4、国内旅客转化率(旅客信息-支付)
    val natinalTravellerRateSQL=
      """
        |select
        |    uniqueFlag(),
        |    1,
        |    0,
        |   sum(case when requestType = '3' then 1 else 0 end)/cast(sum(case when requestType = '2' then 1 else 0 end) as FLOAT),
        |   max(visitTime)
        |from processTag
        |where flightType = 'National'
      """.stripMargin


    //todo:5、国际旅客转化率(旅客信息-支付)
    val internatinalTravellerRateSQL=
      """
        |select
        |    uniqueFlag(),
        |    1,
        |    1,
        |   sum(case when requestType = '3' then 1 else 0 end)/cast(sum(case when requestType = '2' then 1 else 0 end) as FLOAT),
        |   max(visitTime)
        |from processTag
        |where flightType = 'Internatinal'
      """.stripMargin


      //todo:6、爬虫用户转化率(航班选择--->旅客信息)      结果存储mysql的表: nh_flight_query_conversion_rate
      val spiderQueryTravellerRateSQL=
        """
          |select
          |    uniqueFlag(),
          |    0,
          |    1,
          |   sum(case when requestType = '2' then 1 else 0 end)/cast(sum(case when requestType = '1' then 1 else 0 end) as FLOAT),
          |   max(visitTime)
          |from processTag
          |where isSpider = '1'
        """.stripMargin


      //todo:7、正常用户转化率(航班选择--->旅客信息)
      val nonspiderQueryTravellerRateSQL=
        """
          |select
          |    uniqueFlag(),
          |    0,
          |    0,
          |   sum(case when requestType = '2' then 1 else 0 end)/cast(sum(case when requestType = '1' then 1 else 0 end) as FLOAT),
          |   max(visitTime)
          |from processTag
          |where isSpider = '0'
        """.stripMargin


      //todo:8、全部用户转化率(航班选择--->旅客信息)
      val allTravellerRateSQL=
        """
          |select
          |    uniqueFlag(),
          |    0,
          |   sum(case when requestType = '2' then 1 else 0 end)/cast(sum(case when requestType = '1' then 1 else 0 end) as FLOAT),
          |   max(visitTime)
          |from processTag
        """.stripMargin



      //todo:9、爬虫用户转化率(旅客信息-支付)      结果存储mysql的表: nh_flight_query_conversion_rate
      val spiderTravellerPayRateSQL=
        """
          |select
          |    uniqueFlag(),
          |    1,
          |    1,
          |   sum(case when requestType = '3' then 1 else 0 end) / cast(sum(case when requestType = '2' then 1 else 0 end) as FLOAT),
          |   max(visitTime)
          |from processTag
          |where isSpider = '1'
        """.stripMargin


      //todo:10、正常用户转化率(旅客信息-支付)      结果存储mysql的表: nh_flight_query_conversion_rate
      val nonspiderTravellerPayRateSQL=
        """
          |select
          |    uniqueFlag(),
          |    1,
          |    0,
          |   sum(case when requestType = '3' then 1 else 0 end)/cast(sum(case when requestType = '2' then 1 else 0 end) as FLOAT),
          |   max(visitTime)
          |from processTag
          |where isSpider = '0'
        """.stripMargin


      //todo:11、全部用户转化率(旅客信息-支付)    结果存储mysql的表: nh_flight_query_conversion_rate
      val allspiderTravellerPayRateSQL=
        """
          |select
          |    uniqueFlag(),
          |    1,
          |   sum(case when requestType = '3' then 1 else 0 end)/cast(sum(case when requestType = '2' then 1 else 0 end) as FLOAT),
          |   max(visitTime)
          |from processTag
        """.stripMargin


      //todo:12、国内单程、国内双程、国际单程、国际双程查询爬取频次  nh_flight_query_rule
      val spiderFrequencySQL=
        """
          |select
          |    uniqueFlag(),
          |    flightType,
          |    travelType,
          |    count(1),
          |   max(visitTime)
          |from processTag
          |where isSpider = '1'
          |group by flightType,travelType
        """.stripMargin


      //todo:13、爬虫用户查定比(预定与查询的比值)  nh_flow_query_rate
      val spiderBookQueryRateSQL=
        """
          |select
          |    uniqueFlag(),
          |    2,
          |   sum(case when requestType = '1' then 1 else 0 end)/cast(sum(case when requestType = '0' then 1 else 0 end) as FLOAT),
          |   max(visitTime)
          |from processTag
          |where isSpider = '1'
        """.stripMargin

      //todo:14、正常用户查定比(预定与查询的比值)  nh_flow_query_rate
      val nonspiderBookQueryRateSQL=
        """
          |select
          |    uniqueFlag(),
          |    1,
          |   sum(case when requestType = '1' then 1 else 0 end)/cast(sum(case when requestType = '0' then 1 else 0 end) as FLOAT),
          |   max(visitTime)
          |from processTag
          |where isSpider = '0'
        """.stripMargin



    /**
      * 爬虫对系统稳定性的影响
      * 类型：0全部流量，1爬虫流量，2正常流量
    */

  //todo:15、爬虫流量情况      nh_flow_info
      val spiderFlowCountSQL=
        """
          |select
          |    uniqueFlag(),
          |    1,
          |    count(*)
          |from processTag
          |where isSpider = '1'
        """.stripMargin


      //todo:16、正常流量情况  nh_flow_info
      val nonspiderFlowCountSQL=
        """
          |select
          |    uniqueFlag(),
          |    2,
          |    count(1)
          |from processTag
          |where isSpider = '0'
        """.stripMargin


      //todo:17、全部流量情况 nh_flow_info
      val allFlowCountSQL=
        """
          |select
          |    uniqueFlag(),
          |    0,
          |    count(1)
          |from processTag
        """.stripMargin


}
