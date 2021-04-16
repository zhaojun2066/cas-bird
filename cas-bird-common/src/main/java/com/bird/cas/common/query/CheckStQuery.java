package com.bird.cas.common.query;


import java.util.Map;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-08 17:19
 **/
public class CheckStQuery {
    private String st;
    private String ip; // 接入系统的某台实例的ip
    private int port; // 接入系统的端口,ip port ,主要用于通知接入系统销毁自己的session
    private String sign; // 签名
    private String systemClientRequestURI;
  //  private String systemSessionId;
    private Map<String,Object> systemClientAttributes;

//
//    public String getSystemSessionId() {
//        return systemSessionId;
//    }
//
//    public void setSystemSessionId(String systemSessionId) {
//        this.systemSessionId = systemSessionId;
//    }

    public Map<String, Object> getSystemClientAttributes() {
        return systemClientAttributes;
    }

    public void setSystemClientAttributes(Map<String, Object> systemClientAttributes) {
        this.systemClientAttributes = systemClientAttributes;
    }

    public String getSystemClientRequestURI() {
        return systemClientRequestURI;
    }

    public void setSystemClientRequestURI(String systemClientRequestURI) {
        this.systemClientRequestURI = systemClientRequestURI;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
