package com.bird.cas.session.redis;

import com.bird.cas.common.utils.CommonUtils;
import com.bird.cas.session.exception.SessionException;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-14 16:20
 **/
public class RedisManager {
    private  JedisCluster jedisCluster;

    private RedisConfig redisConfig;


    public RedisManager(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
        this.initCluster();
    }

    public  void initCluster(){

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

    public  JedisCluster getJedisCluster(){
        return jedisCluster;
    }

    public void close(){
        jedisCluster.close();
    }


}
