package com.bird.cas.server.demo.service;

import com.bird.cas.server.demo.entity.User;
import org.springframework.stereotype.Service;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-09 09:53
 **/
@Service
public class UserService {
    public User getUserByUsername(String username){
        if ("jufeng".equals(username)){
            User user = new User();
            user.setAge(18);
            user.setId("1");
            user.setNickname("飓风");
            user.setPassword("111111");
            user.setUsername("jufeng");
            user.setTel("13888888888");
            return  user;
        }
        return null;
    }
}
