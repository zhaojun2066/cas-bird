package com.bird.cas.common;


import static com.bird.cas.common.utils.CommonUtils.encode;

/**
 * @program: cas-bird
 * @description: 公共常量
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-14 15:07
 **/
public class PubConstant {



    public static final String LOGIN_SUCCESS_PAGE = "login_success";
    public static final String LOGOUT_SUCCESS_PAGE = "logout_success";


    /*************cas server api**************/
    public static final String CAS_SERVER_CHECK_TICKET_API = "/checkSt";
    public static final String CAS_SERVER_LOGOUT_API = "/logout";
    public static final String CAS_SERVER_DEFAULT_LOGIN_API = "/login";


    /*************cas server form and url params**************/
    public static final String LOGIN_IFRAME_PARAM_NAME = "isframe";
    public static final String LOGIN_EVENT_ID_PARAM_NAME = "_eventId";
    public static final String LOGIN_REMEMBER_ME_PARAM_NAME = "rememberMe";

    public static final String LOGIN_IFRAME_CALLBACK_PARAM = "feedBackUrlCallBack";
    public static final String LOGIN_IFRAME_CALLBACK = "feedBackUrlCallBack";


    /*********cas server cookie******/

    public static final String GT_COOKIE_KEY = "CEGT";
    public static final String GT_COOKIE_PATH = "/";
    public static final String SERVICE_PARAM_NAME = "service";




    /***************cas client session ************************/
    public static final String SESSION_USER_KEY = "currentUser";
    public static final String SESSION_ID_COOKIE_KEY = "CEST";

    /***************cas client redirect url********************/
    public static final String BACK_URL_PARAM = "backurl";


    /*****************redis session*****************************/
    public static final String SESSION_PREFIX = "cas:bird:client:session:";
    public static final String  CREATION_TIME = "creationTime";
    public static final String  LAST_ACCESSED_TIME = "lastAccessedTime";
    public static final String MAX_INACTIVE_INTERVAL = "maxInactiveInterval";


    public static final String PREFIX_GT_KEY = "cas:bird:core:gt:";
    public static final String PREFIX_GT_CLIENT_KEY = "cas:bird:core:gtclient:";
    public static final String PREFIX_ST_KEY = "cas:bird:core:st:";
    public static final String PREFIX_ST_GT_KEY = "cas:bird:core:stgt:";

    public static final String LOGIN_METHOD="login_method";
    public static final String ACCOUNT_TYPE="account_type";
    public static final String IS_SUCCESS="is_success";
    public static final String FAIL_REASON="fail_reason";
    public static final String PRODUCT_NAME="product_name";





}
