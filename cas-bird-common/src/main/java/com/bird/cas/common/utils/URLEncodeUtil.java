package com.bird.cas.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @program: ce-sso
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-28 14:15
 **/
public class URLEncodeUtil {

    public static String encode(String name) throws UnsupportedEncodingException {
        return URLEncoder.encode(name,"UTF-8");
    }

    public static String decode(String url) throws UnsupportedEncodingException{
        return URLDecoder.decode(url,"UTF-8");
    }
}
