package com.bird.cas.session.store.support;

import com.alibaba.fastjson.JSONObject;
import com.bird.cas.common.PubConstant;
import com.bird.cas.common.utils.CommonUtils;
import com.bird.cas.session.redis.RedisManager;
import com.bird.cas.session.store.SessionStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-14 16:26
 **/
public class RedisSessionStore implements SessionStore {

    private RedisManager redisManager;

    private static final String SESSION_PREFIX = PubConstant.SESSION_PREFIX;

    public RedisSessionStore(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    private String key(String sessionId){
        return SESSION_PREFIX + sessionId;
    }

    @Override
    public void setAttribute(String sessionId, String name, Object value) {
        String v = JSONObject.toJSONString(value);
        this.redisManager.getJedisCluster().hset(key(sessionId),name,v);
    }

    @Override
    public Object getAttribute(String sessionId, String name) {
        String v = this.redisManager.getJedisCluster().hget(key(sessionId),name);
        if (!CommonUtils.isNullString(v)){
            return  JSONObject.parseObject(v);
        }
        return null;
    }

    @Override
    public void removeAttribute(String sessionId, String name) {
        this.redisManager.getJedisCluster().hdel(key(sessionId),name);
    }

    @Override
    public List<String> getAttributeNames(String sessionId) {
        Set<String> names = this.redisManager.getJedisCluster().keys(key(sessionId));
        if (names!=null){
            return new ArrayList<>(names);
        }
        return null;
    }

    @Override
    public void removeSession(String sessionId) {
        this.redisManager.getJedisCluster().del(key(sessionId));
    }

    @Override
    public boolean exitSession(String sessionId) {
        return this.redisManager.getJedisCluster().exists(key(sessionId));
    }

    @Override
    public void expire(String sessionId, int expire) {
        this.redisManager.getJedisCluster().expire(key(sessionId),expire);
    }
}
