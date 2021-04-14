package com.bird.cas.core.authentication.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @program: cas-bird
 * @description: 用户名密码方式的凭证
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-07 14:03
 **/
@Data
public class UsernamePasswordCredentials implements Credentials {


    /**
     * 用户名
     */
    @NotNull
    @Size(min=1,message = "请输入用户名")
    private String username;

    /**
     * 密码
     */
    @NotNull
    @Size(min=1, message = "请输入密码")
    private String password;

}
