package com.bird.cas.session.store;

import java.util.List;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-14 15:01
 **/
public interface SessionStore {

    /**
     * 设置属性
     * @param sessionId
     * @param name
     * @param value
     */
    void setAttribute(String sessionId, String name, Object value) ;

    /**
     * 获取属性值
     *
     * @param sessionId
     * @param name
     * @return
     */
    Object getAttribute(String sessionId, String name) ;

    /**
     * 删除属性
     * @param sessionId
     * @param name
     */
    void removeAttribute(String sessionId, String name) ;

    /**
     * 获取所有属性名称
     * @return
     * @param sessionId
     */
    List<String> getAttributeNames(String sessionId) ;

    /**
     * 删除session,那么session 关联的所有属性都被被删除，这个最好使用就是redis 的 hash 来存储
     * @param sessionId
     */
    void removeSession(String sessionId);

    /**
     * 判断session 是否存在
     * @param sessionId
     * @return
     */
    boolean exitSession(String sessionId);


    /**
     * 用于设置session 的失效时长
     * @param sessionId
     * @param expire 失效时长 单位秒
     */
    void expire(String sessionId ,int expire);
}
