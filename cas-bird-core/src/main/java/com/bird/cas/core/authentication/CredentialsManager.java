package com.bird.cas.core.authentication;

import com.bird.cas.core.authentication.annotaion.PrimaryCredentials;
import com.bird.cas.core.authentication.model.Credentials;
import com.bird.cas.core.authentication.model.UsernamePasswordCredentials;
import com.bird.cas.core.exception.CasException;
import com.bird.cas.core.utils.ReadAnnotationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-03 13:48
 **/

@Slf4j
public class CredentialsManager {


    private static Class<?> credentialsClass;

    private static Map<String, Method>  setterMap = new HashMap<>();

    /**
     * 每次请求会获取新的对象用于当前请求
     * @return
     */
    public static Credentials getCredentials(){

        try {
            return  (Credentials)credentialsClass.newInstance();
        } catch (InstantiationException e) {
            log.error(e.getStackTrace().toString());
        } catch (IllegalAccessException e) {
            log.error(e.getStackTrace().toString());
        }
        return null;
    }

    public static void init(){
        initCredentialsClass(); // 查找用户配置的Credentials
        initSetter(); // 缓存Credentials 对应setter方法，供每次调用使用
    }

    static {
        init();
    }



    private static void initCredentialsClass(){
        try {
            Set<Class> classList =  ReadAnnotationUtils.getClazzFromAnnotation("com.bird",PrimaryCredentials.class);
            if (classList!=null && !classList.isEmpty()){
                for (Class<?> c :classList){
                    PrimaryCredentials primaryCredentials =  c.getAnnotation(PrimaryCredentials.class);
                    if (primaryCredentials!=null){
                        credentialsClass = c;
                        break;
                    }
                }
            }
            if (credentialsClass==null){
                credentialsClass = new UsernamePasswordCredentials().getClass();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initSetter(){
        Field[] fields = credentialsClass.getDeclaredFields();
        if (fields!=null && fields.length>0){
            Method[] methods = credentialsClass.getMethods();
            for (Field field: fields){
                String name = field.getName();
                Method method = getGetMethod(methods,name);
                if ( method!=null){
                    setterMap.put(name,method);
                }else {
                    throw new CasException(credentialsClass.getCanonicalName() + " " + name + " not found setter function. ");
                }
            }
        }
    }

    public static   Method getGetMethod(Method[] methods , String name){
        for(int i = 0;i < methods.length;i++){
            if(("set"+name).toLowerCase().equals(methods[i].getName().toLowerCase())){
                return methods[i];
            }
        }
        return null;
    }


    public static  Credentials toCredentials(HttpServletRequest request){
        Credentials credentials = getCredentials();
        try {
            for(Map.Entry<String, Method> entry : setterMap.entrySet()){
                String name = entry.getKey();
                String value = request.getParameter(name);
                if (!StringUtils.isEmpty(value)){
                    Method method = entry.getValue();
                    method.setAccessible(true);
                    method.invoke(credentials,value);
                }
            }
        } catch (IllegalAccessException e) {
            log.error(e.getStackTrace().toString());
        } catch (InvocationTargetException e) {
            log.error(e.getStackTrace().toString());
        }


        return credentials;

    }

}
