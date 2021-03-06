package com.bird.cas.session;

import com.bird.cas.common.utils.CommonUtils;
import com.bird.cas.session.factory.Factory;
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

    private String id = null;
    private long creationTime = 0L;
    private long lastAccessedTime = creationTime;

    private static int maxInactiveInterval = Factory.getSessionConfig().getSessionTimeout();

    private static SessionStore sessionStore = Factory.getSessionStore(); // session具体存储的实现

    private ServletContext servletContext;

    public DistributionHttpSession(String id) {
        this.id = id;
    }

    public DistributionHttpSession( ServletContext servletContext) {
        this.id = CommonUtils.uuid();
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = creationTime;
        this.servletContext = servletContext;
        this.save();
    }



    public DistributionHttpSession(boolean isNew,String sessionId,ServletContext servletContext) {
        this.id =sessionId;
        this.lastAccessedTime = System.currentTimeMillis();// todo: set to store
        this.servletContext = servletContext;
        if (isNew){
            this.creationTime = System.currentTimeMillis();
            this.save();// 刷新session 时长
        }else {
            this.refresh();
        }
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
        this.sessionStore.setAttribute(this.id, MAX_INACTIVE_INTERVAL, interval);
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
        return this.sessionStore.getAttribute(this.id, name);
    }

    @Override
    public Object getValue(String name) {
        return this.getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(this.sessionStore.getAttributeNames(this.id));
    }

    @Override
    public String[] getValueNames() {
        List<String> names = this.sessionStore.getAttributeNames(this.id);
        if (names!=null){
           return names.toArray(new String[0]);
        }

        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.sessionStore.setAttribute(this.id, name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        this.sessionStore.setAttribute(this.id, name, value);
    }

    @Override
    public void removeAttribute(String name) {
        this.sessionStore.removeAttribute(this.id, name);
    }

    @Override
    public void removeValue(String name) {
        this.sessionStore.removeAttribute(this.id, name);
    }

    @Override
    public void invalidate() {
        this.sessionStore.removeSession(this.id);
    }

    @Override
    public boolean isNew() {
        return this.sessionStore.exitSession(this.id);
    }

    private void save(){
        this.setCreationTime(this.creationTime);
        this.setLastAccessedTime(this.lastAccessedTime);
        this.sessionStore.expire(this.id,maxInactiveInterval);
    }

    private void refresh(){
        this.setLastAccessedTime(this.lastAccessedTime);
        this.sessionStore.expire(this.id,maxInactiveInterval);
    }

}
