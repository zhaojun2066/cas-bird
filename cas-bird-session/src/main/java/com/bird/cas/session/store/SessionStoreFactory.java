package com.bird.cas.session.store;

import com.bird.cas.session.redis.RedisConfig;
import com.bird.cas.session.redis.RedisManager;
import com.bird.cas.session.store.support.RedisSessionStore;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-14 17:31
 **/
public class SessionStoreFactory {


    private static  RedisManager redisManager ;


    private static RedisSessionStore redisSessionStore;


    private static void createRedisSessionStore(RedisConfig redisConfig){
        redisManager = new RedisManager(redisConfig);
        redisSessionStore = new RedisSessionStore(redisManager);
    }


    public static SessionStore  getSessionStore(StoreType storeType){
        switch (storeType) {
            case REDIS:
                return redisSessionStore;
            default:
                return null;
        }


    }

}
