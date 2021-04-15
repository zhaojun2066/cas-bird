package com.bird.cas.session.factory;

import com.bird.cas.session.DistributionHttpSession;
import com.bird.cas.session.config.SessionConfig;
import com.bird.cas.session.redis.RedisConfig;
import com.bird.cas.session.redis.RedisManager;
import com.bird.cas.session.store.SessionStore;
import com.bird.cas.session.store.support.RedisSessionStore;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-14 17:31
 **/
public class Factory {

    private static SessionStore sessionStore;


    private static SessionConfig sessionConfig;


    private static RedisConfig redisConfig;



    private static RedisManager redisManager;


    public static void init(SessionConfig sessionConfig){
        Factory.sessionConfig = sessionConfig;
        if (sessionConfig.getRedisConfig()!=null){// 监听 存储 用redis 实现，进行redis 相关的实例化，其他的清另行添加
           redisConfig = sessionConfig.getRedisConfig();
           redisManager = new RedisManager(sessionConfig.getRedisConfig(),sessionConfig);
           createRedisSessionStore();
        }
    }


    public static RedisManager getRedisManager(){
        return  redisManager;
    }


    public static SessionConfig getSessionConfig(){
        return sessionConfig;
    }

    public static RedisConfig getRedisConfig(){
        return  redisConfig;
    }

    private static void createRedisSessionStore(){
        sessionStore = new RedisSessionStore(redisManager);
    }


    public static SessionStore  getSessionStore(){
        return  sessionStore;
    }


    public static HttpSession createHttpSession(String sessionId, ServletContext servletContext){
        return  new DistributionHttpSession(sessionId,servletContext);
    }
    public static HttpSession createHttpSession(ServletContext servletContext){
        return  new DistributionHttpSession(servletContext);
    }

}
