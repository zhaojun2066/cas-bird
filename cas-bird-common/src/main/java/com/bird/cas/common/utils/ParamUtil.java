package com.bird.cas.common.utils;


import com.bird.cas.common.model.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @program: ce-sso
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-08 13:49
 **/
public class ParamUtil {


    public static List<Param> getQueryParamList(String queryString){
        if (CommonUtils.isNullString(queryString)){
            return new ArrayList<>(0);
        }
        List<Param> params = new ArrayList<>();
        if (!queryString.contains("&")){
            String qs [] = queryString.split("=");
            if (qs!=null && qs.length>0){
                Param p = new Param();
                p.setKey(qs[0]);
                if (qs.length>1){
                    p.setValue(qs[1]);
                }
                params.add(p);
            }

        }else {
            String [] keyvalues = queryString.split("&");
            if (keyvalues!=null && keyvalues.length>0){
                for (String kvs : keyvalues){
                    String qs [] = kvs.split("=");
                    if (qs!=null && qs.length>0){
                        Param p = new Param();
                        p.setKey(qs[0]);
                        if (qs.length>1){
                            p.setValue(qs[1]);
                        }
                        params.add(p);
                    }
                }
            }

        }

        return params;
    }

    public static String getQueryParamsWithoutTicket(HttpServletRequest request,String ticketParamName){
        StringBuilder param = new StringBuilder();
        String queryString = request.getQueryString();
        if (CommonUtils.isNullString(queryString)){
            return null;
        }
        List<Param> params = getQueryParamList(queryString);
        if (!params.isEmpty()){
            for (Param p : params){
                String name = p.getKey();
                if (!CommonUtils.isNullString(name) && ticketParamName.equals(name)){
                    continue;
                }

                String value = p.getValue();
                param.append(name).append("=").append(value).append("&");
            }
        }

        if (param.lastIndexOf("&")>0){
            return param.substring(0,param.toString().lastIndexOf("&"));
        }

        return param.toString();
    }

    public static String getQueryParams(HttpServletRequest request,boolean service,boolean loginUri){
        StringBuilder param = new StringBuilder();
        String queryString = request.getQueryString();
        if (CommonUtils.isNullString(queryString)){
            return null;
        }
        List<Param> params = getQueryParamList(queryString);
        if (!params.isEmpty()){
            for (Param p : params){
                String name = p.getKey();
                if (!service && !CommonUtils.isNullString(name) && "service".equals(name)){
                    continue;
                }
                if (!loginUri && !CommonUtils.isNullString(name) && "loginUri".equals(name)){
                    continue;
                }
                String value = p.getValue();
                param.append(name).append("=").append(value).append("&");
            }
        }

        if (param.lastIndexOf("&")>0){
            return param.substring(0,param.toString().lastIndexOf("&"));
        }

        return param.toString();
    }


    public static String getQueryParams(HttpServletRequest request,boolean service){
        // a=12&c=12&d=126
        StringBuilder param = new StringBuilder();
        String queryString = request.getQueryString();
        if (CommonUtils.isNullString(queryString)){
            return null;
        }
        List<Param> params = getQueryParamList(queryString);
        if (!params.isEmpty()){
            for (Param p : params){
                String name = p.getKey();
                if (!service && !CommonUtils.isNullString(name) && "service".equals(name)){
                    continue;
                }
                String value = p.getValue();
                param.append(name).append("=").append(value).append("&");
            }
        }

        if (param.lastIndexOf("&")>0){
          return param.substring(0,param.toString().lastIndexOf("&"));
        }

        return param.toString();
    }
}
