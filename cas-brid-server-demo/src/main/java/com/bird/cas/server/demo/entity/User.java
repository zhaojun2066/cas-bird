package com.bird.cas.server.demo.entity;

import lombok.Data;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-09 09:54
 **/
@Data
public class User {

    private String id;
    private int age;
    private String tel;
    private String nickname;

    private String username;
    private String password;
}
