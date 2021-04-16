package com.bird.cas.common.utils;


/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-07 11:15
 **/
public class SignUtil {
    public static final String SING_KEY = "1q2w3e4r$12";

    public static String getSign(String value){
        return  MD5Util.MD5(SING_KEY+value.toString()+SING_KEY);
    }
}
