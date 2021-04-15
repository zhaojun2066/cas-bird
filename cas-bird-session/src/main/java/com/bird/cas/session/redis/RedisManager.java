package com.bird.cas.session.redis;

import com.bird.cas.common.utils.CommonUtils;
import com.bird.cas.session.config.SessionConfig;
import com.bird.cas.session.exception.SessionException;
import com.bird.cas.session.listener.RedisMessageListener;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-14 16:20
 **/
public class RedisManager {
    private  JedisCluster jedisCluster;

    private RedisConfig redisConfig;

    private SessionConfig sessionConfig;


    public RedisManager(RedisConfig redisConfig, SessionConfig sessionConfig) {
        this.redisConfig = redisConfig;
        this.sessionConfig = sessionConfig;
        this.initCluster();
        this.subscribe();
    }

    private   void initCluster(){

        List<RedisHost> hostList = redisConfig.getHosts();
        if (hostList.isEmpty()){
            throw new SessionException("redis host is empty");
        }
        // 创建集群的节点集合
        Set<HostAndPort> nodes = new HashSet<>();
        for (RedisHost host: hostList){
            nodes.add(new HostAndPort(host.getHost(), host.getPort()));
        }

        // 设置Redis Pool相关参数
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisConfig.getMaxTotal());
        poolConfig.setMaxIdle(redisConfig.getMaxIdle());
        poolConfig.setMinIdle(redisConfig.getMinIdle());
        poolConfig.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
        poolConfig.setTestOnBorrow(redisConfig.isTestOnBorrow());
        poolConfig.setTestOnReturn(redisConfig.isTestOnReturn());
        poolConfig.setTestWhileIdle(redisConfig.isTestWhileIdle());
        // 利用上面的集群节点nodes和poolConfig，创建redis集群连接池，并获取一个redis连接
        String pwd = redisConfig.getPassword();
        if (CommonUtils.isNullString(pwd)){
            jedisCluster = new JedisCluster(nodes, poolConfig);
        }else {
            jedisCluster = new JedisCluster(nodes,2000,2000,3,pwd,poolConfig);
        }

    }

    private   ExecutorService exec;

    private  JedisPubSub jedisPubSub;


    private   void subscribe(){
        jedisPubSub = new RedisMessageListener(sessionConfig.getSessionListener());
        exec = Executors.newFixedThreadPool(10,
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = Executors.defaultThreadFactory().newThread(r);
                        t.setDaemon(true);
                        return t;
                    }
                });
        // 监听每个redis 节点 key 的失效事件
        Map<String, JedisPool> clusterNodes =  getJedisCluster().getClusterNodes();
        for (String key : clusterNodes.keySet()) {
            JedisPool jedisPool = clusterNodes.get(key);
            final Jedis jedisConn = jedisPool.getResource();
            try {
                exec.submit(() -> {
                    jedisConn.subscribe(jedisPubSub, "__keyevent@0__:expired");
                    jedisConn.subscribe(jedisPubSub, "__keyevent@0__:del");
                    jedisConn.subscribe(jedisPubSub, "__keyevent@0__:set");
                });

            }finally {
                //  jedisConn.close();
            }
        }
    }

    public  JedisCluster getJedisCluster(){
        return jedisCluster;
    }

    public void close(){
        jedisCluster.close();
    }


}
