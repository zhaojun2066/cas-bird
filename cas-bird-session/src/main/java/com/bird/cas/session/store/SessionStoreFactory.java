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


    private static final RedisConfig redisConfig;
    private static final RedisManager redisManager ;

    static {
        redisConfig = new RedisConfig();
        redisManager = new RedisManager(redisConfig);
    }

    public static SessionStore  getSessionStore(StoreType storeType){
        switch (storeType) {
            case REDIS:
                return new RedisSessionStore(redisManager);
            default:
                return null;
        }


    }

}
