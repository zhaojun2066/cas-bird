package com.bird.cas.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-03 16:55
 **/
public class CookieUtil {
    // 默认缓存时间,单位/秒, 2H
    private static final int COOKIE_MAX_AGE = Integer.MAX_VALUE;

    /**
     * 保存
     *
     * @param response
     * @param key
     * @param value
     * @param ifRemember
     */
//    public static void set(HttpServletResponse response, String key, String value, boolean ifRemember) {
//        int age = ifRemember?COOKIE_MAX_AGE:-1;
//        set(response, key, value, null, COOKIE_PATH, age, true);
//    }

    public static void set(HttpServletResponse response, String domain,String key, String value,String path, boolean ifRemember) {
        int age = ifRemember?COOKIE_MAX_AGE:-1;
        set(response, key, value, domain, path, age, true);
    }


    /**
     * 保存
     *
     * @param response
     * @param key
     * @param value
     * @param maxAge
     */
    public static void set(HttpServletResponse response, String key, String value, String domain, String path, int maxAge, boolean isHttpOnly) {
        Cookie cookie = new Cookie(key, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(isHttpOnly);
        response.addCookie(cookie);
    }

     public static void setCookieWithSameSite(HttpServletResponse response,HttpServletRequest request,
                                              String key, String value,String path,
                                              boolean isHttpOnly, boolean secure){
         StringBuilder sb = new StringBuilder();
         sb.append(key).append('=');
         sb.append(value);
         sb.append("; Domain=").append(request.getServerName());
         sb.append("; Path=").append(path);

         if (isHttpOnly) {
             sb.append("; HttpOnly");
         }

         if (secure) {
             sb.append("; Secure");
         }

         if (secure /*&&
                 isSameBrowser(request)*/) {
             sb.append("; SameSite=").append("NONE ");
         }

         response.addHeader("Set-Cookie", sb.toString());
     }

    /**
     * 是否 https 协议
     * @param request
     * @return
     */
    private static boolean isSecureCookie(HttpServletRequest request) {
        return OriginalRequestUtil.isSecureSchema(request);
    }

    private static boolean isSameBrowser(HttpServletRequest request) {
        return OriginalRequestUtil.isSameBrowser(request);
    }


    /**
     * 查询value
     *
     * @param request
     * @param key
     * @return
     */
    public static String getValue(HttpServletRequest request, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 查询Cookie
     *
     * @param request
     * @param key
     */
    private static Cookie get(HttpServletRequest request, String key) {
        Cookie[] arr_cookie = request.getCookies();
        if (arr_cookie != null && arr_cookie.length > 0) {
            for (Cookie cookie : arr_cookie) {
                if (cookie.getName().equals(key)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 删除Cookie
     * @param request
     * @param response
     * @param key
     */
    public static void remove(HttpServletRequest request, HttpServletResponse response,String path, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            set(response, key, "", request.getServerName(), path, 0, true);
        }
    }

    public static void remove(HttpServletRequest request, HttpServletResponse response,String path, String key
    ,boolean secure) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            setCookieWithSameSite(response,request,key,"",path,true,secure);
        }
    }
}
