package com.bird.cas.core.ticket;

import com.bird.cas.common.PubConstant;
import com.bird.cas.common.utils.CommonUtils;
import com.bird.cas.core.authentication.model.Principal;
import com.bird.cas.core.config.CasConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.bird.cas.common.PubConstant.*;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-07 17:11
 **/
@Slf4j
@Component
public class TicketManager {

    @Autowired
    private CasConfigProperties casConfig;

    @Autowired
    private RedisTemplate<String,Object> redisTemplateObject;

    @Autowired
    private ValueOperations<String,Object> valueOperations;

    @Autowired
    private SetOperations<String, Object> setOperations;

    private String getGTKey(String gt){
        return PubConstant.PREFIX_GT_KEY + gt;
    }
    private String getSTKey(String st){
        return PREFIX_ST_KEY+st;
    }

    private String getGTClientKey(String gt){
        return PREFIX_GT_CLIENT_KEY + gt;
    }

    /**
     * 创建全局票据，并设置到redis 内
     * @param principal
     * @return 全局票据
     */
    public GTInfo createGT(Principal principal){
        GTInfo gtInfo = new GTInfo();
        gtInfo.setId(principal.getId());
        gtInfo.setAttributes(principal.getAttributes());
        String gt = CommonUtils.uuid();
        String key = getGTKey(gt);
        gtInfo.setGt(gt);
        valueOperations.set(key,gtInfo,casConfig.getGtExpireTime(), TimeUnit.SECONDS);
        return gtInfo;
    }

    /**
     * 创建 st
     * @return
     */
    public String createST(GTInfo gtInfo) {
        STInfo principalInfo = new STInfo();
        principalInfo.setGt(gtInfo.getGt());
        principalInfo.setId(gtInfo.getId());
        principalInfo.setAttributes(gtInfo.getAttributes());
        String st =CommonUtils.uuid();
        String key = getSTKey(st);
        valueOperations.set(key,principalInfo,casConfig.getStExpireTime(),TimeUnit.SECONDS);
        return st;
    }

    public STInfo getSTInfoByST(String st) {
        String key = getSTKey(st);
        return (STInfo)valueOperations.get(key);
    }

    /**
     * 根据全局票据 获取用户key
     * @param gt
     * @return
     */
    public GTInfo getGTInfoByGT(String gt) {
        String key = getGTKey(gt);
        return  (GTInfo)valueOperations.get(key);
    }


    /**
     * 删除gt
     * @param gt
     */
    public void removeGT(String gt) {
        String key = getGTKey(gt);
        redisTemplateObject.delete(key);
    }

    /**
     * 注销GT 操作
     * @param gt
     */
    public void logoutGT(String gt){
        removeGT(gt); // 删除全局票据
        if (log.isDebugEnabled()){
            log.debug("logout gt {} ",gt);
        }
    }


    public void  addGTSystemClient(String gt,SystemClient systemClient){
        String key = getGTClientKey(gt);
        setOperations.add(key,systemClient);
        // 重新设置gtClient 失效时间
        redisTemplateObject.expire(key,casConfig.getGtClientExpireTime(), TimeUnit.SECONDS);
        // 重新设置全局票据失效时间
        String gtKey = getGTKey(gt);
        redisTemplateObject.expire(gtKey,casConfig.getGtExpireTime(),TimeUnit.SECONDS);
        /*if (log.isDebugEnabled()){
            log.debug("gt {} , add client sessionId {} ",gt,client.getSessionId());
        }*/
    }
}
