package com.bird.cas.session.listener;

import com.bird.cas.common.PubConstant;
import com.bird.cas.common.utils.CommonUtils;
import com.bird.cas.session.DistributionHttpSession;
import redis.clients.jedis.JedisPubSub;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-15 15:04
 **/
public class RedisMessageListener extends JedisPubSub {

    private HttpSessionListener httpSessionListener;

    public RedisMessageListener(HttpSessionListener httpSessionListener) {
        this.httpSessionListener = httpSessionListener;
    }

    @Override
    public void onMessage(String channel, String message) {
        String sessionId=null;
        if (!CommonUtils.isNullString(message)){
            if (message.startsWith(PubConstant.SESSION_PREFIX)){
                sessionId = message.replace(PubConstant.SESSION_PREFIX,"");
            }
        }

        if(!CommonUtils.isNullString(sessionId)){
            HttpSession httpSession = new DistributionHttpSession(sessionId);
            HttpSessionEvent event = new HttpSessionEvent(httpSession);

            if ("__keyevent@0__:expired".equals(channel) || "__keyevent@0__:del" .equals(channel)){
                this.httpSessionListener.sessionDestroyed(event);
            }else if ("__keyevent@0__:set".equals(channel)){
                this.httpSessionListener.sessionCreated(event);
            }
        }
    }
}