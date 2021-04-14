package com.bird.cas.core.authentication.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @program: cas-bird
 * @description: 用于保存登录成功后的用户信息
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-07 14:08
 **/
public interface Principal extends Serializable {


    boolean isPass();

    /**
     * 返回唯 Principal 一的id 标识
     * @return
     */
    String getId();

    /**
     * 返回其他的属性信息
     * @return
     */
    Map<String, Object> getAttributes();
}
