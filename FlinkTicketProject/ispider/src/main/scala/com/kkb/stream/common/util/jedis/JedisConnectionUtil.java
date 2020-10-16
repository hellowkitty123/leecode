package com.kkb.stream.common.util.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.*;


public class JedisConnectionUtil {

    /**
     * jedis哨兵集群连接单例对象
     */
    private static JedisSentinelPool jedisSentinelPool = null;

    /**
     * jedis集群连接单例对象
     */
    private static JedisCluster jedisCluster = null;

    private static Jedis jedis = null;


    /**
     * 创建JedisCluster
     * JedisCluster不需要单独构建连接池，其已经基于连接池实现
     */
    private static void createJedisCluster(){
        //读取配置文件
        Properties prop = PropertiesUtil.getProperties();
        //jedisCluster配置
        String[] serverArray = prop.getProperty("redis.servers").split(",");
        int connectionTimeout = Integer.valueOf(prop.getProperty("connectionTimeout"));
        int soTimeout = Integer.valueOf(prop.getProperty("soTimeout"));
        int maxAttempts = Integer.valueOf(prop.getProperty("maxAttempts"));
        String password = String.valueOf(prop.getProperty("password"));
        //jedis连接池配置
        // 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(new Integer(prop.getProperty("maxTotal")));
        // 设置最大空间连接数
        config.setMaxIdle(new Integer(prop.getProperty("maxIdle")));
        // 设置最小空间连接数
        config.setMinIdle(new Integer(prop.getProperty("minIdle")));
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();

        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }

        //注意：这里超时时间不要太短，他会有超时重试机制
        jedisCluster = new JedisCluster(nodes, connectionTimeout, soTimeout, maxAttempts,config);

//        jedisCluster = new JedisCluster(nodes, connectionTimeout, soTimeout, maxAttempts,password,config);
    }

    private static void createJedis(){
        Properties prop = PropertiesUtil.getProperties();
        String[] serverArray = prop.getProperty("redis.servers").split(",");
        String password = String.valueOf(prop.getProperty("password"));
        jedis = new Jedis(serverArray[0]);
        jedis.auth(password);
    }


    /**
     * 创建sentinel连接池
     */
    private static void createJedisSentinelPool(){
        //读取jedis配置文件
        Properties prop = PropertiesUtil.getProperties();
        //jedis连接池配置
        // 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(new Integer(prop.getProperty("maxTotal")));
        // 设置最大阻塞时间，记住是毫秒数milliseconds
        config.setMaxWaitMillis(new Integer(prop.getProperty("maxWaitMillis")));
        // 设置最大空间连接数
        config.setMaxIdle(new Integer(prop.getProperty("maxIdle")));
        // 设置最小空间连接数
        config.setMinIdle(new Integer(prop.getProperty("minIdle")));
        // jedis实例是否可用
        boolean testOnBorrow = prop.getProperty("testOnBorrow") == "false" ? false : true;
        config.setTestOnBorrow(testOnBorrow);
        //#从连接池获取不到连接则阻塞
        boolean blockWhenExhausted = prop.getProperty("blockWhenExhausted") == "false" ? false : true;
        config.setBlockWhenExhausted(blockWhenExhausted);
        //#连接对象后进先出
        boolean lifo = prop.getProperty("lifo") == "false" ? false : true;
        config.setLifo(lifo);
        //#归还连接到池时测试连接
        boolean testOnReturn = prop.getProperty("testOnReturn") == "false" ? false : true;
        config.setTestOnReturn(testOnReturn);
        //#测试连接池空闲的连接
        boolean testWhileIdle = prop.getProperty("testWhileIdle") == "false" ? false : true;
        config.setTestWhileIdle(testWhileIdle);
        //#测试连接池空闲连接的时间间隔，testWhileIdle=true时生效
        config.setTimeBetweenEvictionRunsMillis(new Integer(prop.getProperty("timeBetweenEvictionRunsMillis")));
        //获取redis密码
        //String password = prop.getProperty("PASSWORD");
        String masterName = prop.getProperty("MASTER");
        String sentinel_1 = prop.getProperty("SENTINEL_1");
        String sentinel_2 = prop.getProperty("SENTINEL_2");
        String sentinel_3 = prop.getProperty("SENTINEL_3");
        Set<String> sentinels = new HashSet<String>();
        sentinels.add(sentinel_1);
        sentinels.add(sentinel_2);
        sentinels.add(sentinel_3);
        jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, config);
    }

    /**
     * 在多线程环境同步初始化
     */
    private static synchronized void JedisClusterInit(){
        if (jedisCluster == null) {
            createJedisCluster();
        }
    }

    /**
     * 在多线程环境同步初始化
     */
    private static synchronized void sentinelPoolInit(){
        if (jedisSentinelPool == null)
            createJedisSentinelPool();
    }

    /**
     * 在多线程环境同步初始化
     */
    private synchronized static void JedisInit() {
        if (jedis == null)
            createJedis();
    }
    /**
     * 获取一个jedis对象
     * @return
     */
    public static Jedis getJedis(){
        if (jedisSentinelPool == null)
            sentinelPoolInit();
        return jedisSentinelPool.getResource();
    }

    /**
     * 获取一个jedis对象
     * @return
     */
    public static JedisCluster getJedisCluster(){
        if (jedisCluster == null){
            JedisClusterInit();
        }
        return jedisCluster;
    }


    /**
     * 释放一个连接
     * @param jedis
     */
    public static void returnRes(Jedis jedis){
        jedisSentinelPool.returnResource(jedis);
    }

    /**
     * 销毁一个连接
     * @param jedis
     */
    public static void returnBrokenRes(Jedis jedis){
        jedisSentinelPool.returnBrokenResource(jedis);
    }

    /**
     * 获取集群上所有key
     * @param pattern
     * @return
     */
    public static TreeSet<String> keys(JedisCluster jc, String pattern){
        TreeSet<String> keys = new TreeSet<String>();
        Map<String, JedisPool> clusterNodes = jc.getClusterNodes();
        for(String k : clusterNodes.keySet()){
            JedisPool jp = clusterNodes.get(k);
            Jedis jedis = jp.getResource();
            try {
                keys.addAll(jedis.keys(pattern));
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                //用完一定要close这个链接！！！
                jedis.close();
            }
        }
        return keys;
    }

    /**
     * 连接redis方法
     * @param args
     */
    public static void main(String[] args) throws IOException {
        JedisCluster jedisCluster=getJedisCluster();
//        jedisCluster.set("BlackChangeFlag", "false");
//        jedisCluster.set("ProcessChangeFlag", "false");
//        jedisCluster.set("FilterChangeFlag", "false");
//        jedisCluster.set("AnalyzeRuleChangeFlag", "false");
//        jedisCluster.set("QueryCriticalPagesChangeFlag", "false");
//        jedisCluster.set("BookCriticalPagesChangeFlag", "false");
//        jedisCluster.set("ClassifyRuleChangeFlag", "false");
        jedisCluster.set("ExtractDataChangeFlag", "true");
//        jedisCluster.set("dang", "no");
        //System.out.println("sjk============" + jedisCluster.get("sjk"));
        TreeSet<String> keySets = keys(jedisCluster, "*ChangeFlag");
        //使用完成后不要调用close，将会导致无法继续使用该对象
        //jedisCluster.close();
        for(String k: keySets){
            System.out.println(k + " ============ " + jedisCluster.get(k));
        }

    }

}
