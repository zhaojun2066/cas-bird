package com.bird.cas.session;

import com.bird.cas.common.utils.CommonUtils;
import com.bird.cas.session.store.SessionStore;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.io.Serializable;
import java.util.*;

import static com.bird.cas.common.PubConstant.*;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-14 14:31
 **/
public class DistributionHttpSession implements HttpSession, Serializable {

    public static final int DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS = 3600;



    private String id = null;
    private long creationTime = 0l;
    private long lastAccessedTime = creationTime;

    private static int maxInactiveInterval;


    private SessionStore sessionStore; // session具体存储的实现


    private ServletContext servletContext;


    public DistributionHttpSession(SessionStore sessionStore, ServletContext servletContext) {
        this.id = CommonUtils.uuid();
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = creationTime;
        this.sessionStore = sessionStore;
        this.servletContext = servletContext;
        this.refresh();// 刷新session 时长

    }

    public DistributionHttpSession(String sessionId,SessionStore sessionStore, ServletContext servletContext) {
        this.id =sessionId;
        this.lastAccessedTime = System.currentTimeMillis();// todo: set to store
        this.sessionStore = sessionStore;
        this.servletContext = servletContext;
        this.refresh();// 刷新session 时长

    }

    public void setCreationTime(long time){
        this.setAttribute(CREATION_TIME,time);
    }

    public void setLastAccessedTime(long time){
        this.setAttribute(LAST_ACCESSED_TIME,time);
    }


    @Override
    public long getCreationTime() {
        return (Long)this.getAttribute(CREATION_TIME);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public long getLastAccessedTime() {
        return (Long)this.getAttribute(LAST_ACCESSED_TIME);
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.sessionStore.setAttribute(MAX_INACTIVE_INTERVAL,interval);
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    /** @deprecated */
    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return this.sessionStore.getAttribute(name);
    }

    @Override
    public Object getValue(String name) {
        return this.getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(this.sessionStore.getAttributeNames());
    }

    @Override
    public String[] getValueNames() {
        List<String> names = this.sessionStore.getAttributeNames();
        if (names!=null){
           return names.toArray(new String[0]);
        }

        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.sessionStore.setAttribute(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        this.sessionStore.setAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        this.sessionStore.removeAttribute(name);
    }

    @Override
    public void removeValue(String name) {
        this.sessionStore.removeAttribute(name);
    }

    @Override
    public void invalidate() {
        this.sessionStore.removeSession(this.id);
    }

    @Override
    public boolean isNew() {
        return this.sessionStore.exitSession(this.id);
    }

    private void refresh(){
        this.setCreationTime(this.creationTime);
        this.setLastAccessedTime(this.lastAccessedTime);
        this.sessionStore.expire(this.id,maxInactiveInterval);
    }

}
