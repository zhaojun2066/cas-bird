


## cas-bird-core
     核心实现
     入口在AuthenticationController.java，包括登录登出操作
     
## cas-bird-common
    公共工具类

##   cas-brid-server-demo

单点server demo       
##### 引入依赖  
 spring-boot-starter-thymeleaf 用于页面 
```xml
<dependencies>
        <dependency>
            <groupId>com.bird.cloud</groupId>
            <artifactId>spring-cloud-starter-bird-base</artifactId>
        </dependency>
        <dependency>
            <groupId>com.bird.cas</groupId>
            <artifactId>cas-bird-core</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
    </dependencies>
```
##### 实现AbstractAuthenticationService 抽象类
     实现   public Principal authenticate(Credentials credentials)  方法，用于验证用户
     
     Credentials 默认实现是UsernamePasswordCredentials，只是用户名和密码 ，如果需要验证码之类的
     可以实现Credentials 接口 ，如：需要加上PrimaryCredentials 注解，就可以替换默认的UsernamePasswordCredentials了
     @PrimaryCredentials
     MyCredentials implements Credentials{}
     
     Principal 主要是登录成功后用户的基本信息，默认实现SimplePrincipal，基本够用，也可以实现Principal 接口，定义自己
     的用户信息
     
```java
@Override
public Principal authenticate(Credentials credentials) {
    //这里如果你实现了Credentials 接口，请转换为你自己的实现类
    //如果没有实现，则使用默认的 UsernamePasswordCredentials.java
    UsernamePasswordCredentials usernamePasswordCredentials = (UsernamePasswordCredentials)credentials;
    String username = usernamePasswordCredentials.getUsername();
    String pwd = usernamePasswordCredentials.getPassword();
    User user = userService.getUserByUsername(username);
    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(pwd)){
        throw  new CasException("请输入用户名和密码");
    }
    if ( user==null){
        throw  new CasException("用户不存在");
    }
    if (!user.getPassword().equals(pwd)){
        throw  new CasException("用户名和密码匹配");
    }
    // 这里是我使用了默认的Principal ，如果需要扩展可以实现 Principal 接口
    Map<String,Object> userProperty = new HashMap<>();
    userProperty.put("nickname",user.getNickname());
    userProperty.put("age",user.getAge());
    userProperty.put("tel",user.getTel());
    SimplePrincipal principal = new SimplePrincipal(user.getId(),userProperty);
    principal.setPass(true); // 表示验证通过，通过设置为true 就可以了
    return principal;
}
```

##### 配置文件

redis 配置主要是用于存储票据


```yaml
spring:
  profiles: local
  redis:
    timeout: 3000
#    password: redis@q1w2e3
    jedis:
      pool:
        minIdle: 5
        maxIdle: 10 #最大空闲数
        maxTotal: 25 #控制一个pool可分配多少个jedis实例,用来替换上面的redis.maxActive,如果是jedis 2.4以后用该属性
        maxWaitMillis: 1000 #最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。
        minEvictableIdleTimeMillis: 300000 #连接的最小空闲时间 默认1800000毫秒(30分钟)
        timeBetweenEvictionRunsMillis: 30000 #逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        testOnBorrow: true #是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        testWhileIdle: true #在空闲时检查有效性, 默认false
    cluster:
      nodes: 10.12.40.253:7000, 10.12.40.252:7000, 10.12.40.251:7000
      max-redirects: 2



server:
  port: 9200


bird:
  cas:
    gtExpireTime: 10080 # 全局票据的失效时长
    stExpireTime: 300  # 临时票据的失效时长
    gtClientExpireTime: 10090 # 全局票据关联的客户端时长
    passwordEncode: false # 没用
    cookieSecure: false  # 是否需要https
    asynctask: # 异步通知接入系统登出操作
      corePoolSize: 30
      maxPoolSize: 1000
      queueCapacity: 3000
      waitForTasksToCompleteOnShutdown: true
      awaitTerminationSeconds: 3000
      ThreadNamePrefix: 300-sso-asynctask-
    whiteDomains: # 登录成功后redirect 的白名单
      - "localhost"
      - "www.baidu.com"
      - "localhost:9200"
```


如果要接入 oauth2 的三方登录，请参考  oauth2.md 