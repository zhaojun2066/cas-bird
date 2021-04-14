package com.bird.cas.core.context;


import com.bird.cas.core.authentication.model.Credentials;

import java.util.HashMap;
import java.util.Map;

import static com.bird.cas.common.PubConstant.BACK_URL_PARAM;
import static com.bird.cas.common.PubConstant.SERVICE_PARAM_NAME;


/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-21 14:18
 **/
public class LoginContextHandler {
    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key){
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        return map.get(key);
    }


    public static String getService(){
       return (String) get(SERVICE_PARAM_NAME);
    }

    public static void  setService(String service){
        set(SERVICE_PARAM_NAME,service);
    }

    public static void setCredentials(Credentials credentials){
        set("credentials",credentials);
    }

    public static Credentials getCredentials(){
        return  (Credentials)get("credentials");
    }

    public static void setLoginPage(String page){
        set("loginPage",page);
    }

    public static String getLoginPage(){
        return (String) get("loginPage");
    }

    public static void remove(){
        threadLocal.remove();
    }

    public static void setBackUrl(String backUrl){
        set(BACK_URL_PARAM,backUrl);
    }

    public static String getBackUrl(){
            return (String) get(BACK_URL_PARAM);
    }

}
