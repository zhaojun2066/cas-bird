package com.bird.cas.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-16 11:18
 **/
public class CommonUtils {
    public  static boolean isNullString(String value){
        if (value==null || "".equals(value)){
            return true;
        }
        return false;
    }


    public static byte[] encode(String v){
        try {
            return v.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String uuid(){
        return UUID.randomUUID().toString();
    }
}
