package com.bird.cas.session.config;

import com.bird.cas.common.utils.CommonUtils;
import com.bird.cas.session.redis.RedisConfig;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-14 17:53
 **/
public class SessionConfig {

    //
    private String sessionIdName;


    private int sessionTimeout = 24*60*3600;

    private String sessionListenerClass;
    private String sessionAttributeListenerClass;


    private HttpSessionListener sessionListener;
    private HttpSessionAttributeListener sessionAttributeListener;


    private RedisConfig redisConfig;

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public String getSessionListenerClass() {
        return sessionListenerClass;
    }

    public void setSessionListenerClass(String sessionListenerClass) {
        this.sessionListenerClass = sessionListenerClass;
        if (!CommonUtils.isNullString(sessionListenerClass)){
            try {
                Class clazz = Class.forName(sessionListenerClass);
                sessionAttributeListener = (HttpSessionAttributeListener)clazz.newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public String getSessionAttributeListenerClass() {
        return sessionAttributeListenerClass;
    }

    public void setSessionAttributeListenerClass(String sessionAttributeListenerClass) {
        this.sessionAttributeListenerClass = sessionAttributeListenerClass;
        if (!CommonUtils.isNullString(sessionAttributeListenerClass)){
            try {
                Class clazz = Class.forName(sessionAttributeListenerClass.trim());
                sessionAttributeListener = (HttpSessionAttributeListener)clazz.newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }

    }

    public HttpSessionListener getSessionListener() {
        return sessionListener;
    }

    public HttpSessionAttributeListener getSessionAttributeListener() {
        return sessionAttributeListener;
    }

    public String getSessionIdName() {
        return sessionIdName;
    }

    public void setSessionIdName(String sessionIdName) {
        this.sessionIdName = sessionIdName;
    }
}
