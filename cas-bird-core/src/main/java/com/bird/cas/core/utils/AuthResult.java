package com.bird.cas.core.utils;

import com.bird.cas.common.PubConstant;
import lombok.Data;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-07 16:38
 **/
@Data
public class AuthResult {
    private String st;
    private String gt;
    private boolean pass;


    public static AuthResult success(String st,String gt){
        AuthResult authStatus = new AuthResult();
        authStatus.setSt(st);
        authStatus.setGt(gt);
        authStatus.setPass(true);
        return authStatus;
    }

    public static AuthResult fail(){
        AuthResult authStatus = new AuthResult();
        authStatus.setPass(false);
        return authStatus;
    }



}
