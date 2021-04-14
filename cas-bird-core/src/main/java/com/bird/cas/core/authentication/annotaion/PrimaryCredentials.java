package com.bird.cas.core.authentication.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: cas-bird
 * @description: 用于指定Credentials 的实现类，不指定默认就是 UsernamePasswordCredentials.java
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-07 15:17
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryCredentials {
}
