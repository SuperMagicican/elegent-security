![](doc/images/logob.png)

# ElegentSecurity【优雅权限框架】

## 介绍

​	ElegentSecurity是传智教育研究院研发的一款的基于令牌的轻量级权限框架。 这个框架可以让你更优雅地在项目中实现权限控制。这个组件具有的特点：

1. 基于令牌的轻量级的权限框架，适用于前后端分离的项目架构。
2. 支持访问地址的拦截和注解方式的拦截。
3. 既适用于基于微服务网关的分布式架构，也适用于单体的Springboot微服务架构。
4. 所有设计均采用组件化，用户可以自己进行定制开发和扩展。

## 框架集成与配置

### 单体架构

（1）项目引入依赖：

```xml
<!--地址拦截模块 -->
<dependency>
    <groupId>cn.elegent.security</groupId>
    <artifactId>elegent-security-verifier-web</artifactId>
    <version>1.1.0</version>
</dependency>
<!--令牌签发模块-->
<dependency>
    <groupId>cn.elegent.security</groupId>
    <artifactId>elegent-security-token</artifactId>
    <version>1.1.0</version>
</dependency>
<!--注解支持-->
<dependency>
    <groupId>cn.elegent.security</groupId>
    <artifactId>elegent-security-annotation</artifactId>
</dependency>
```
（2）配置文件   参考以下格式

```yaml
elegent:
  security:
    login-strategies:
      - type: admin
        strategy: username_password
        secret-key: elegent
        ttl: 2
    verifier:
      header:
        type: login-type
        token: user-token
      ignore-url:  # 忽略地址，不用登录也可以访问的地址
        - POST/login
        - POST/logout
      privilege-url: #特权地址，所有登录用户都可以访问的地址
        - GET/user/list
        - GET/goods/list
      privilege-user:
        - admin
```

配置说明：

login-strategies:  定义登录策略组，下面可以有多个登录策略。登录策略包括以下配置：

-- type:    登录策略类型名称，用户自定义。

-- strategy： 登录策略表达式。有如下几个值: username 、 password 、code 、mobile  

例如一个系统有多种登录入口，管理员通过用户名密码登录，那么策略表达式就是username_password ，普通员工通过手机号验证码进行登录，那么策略表达式就是 mobile_code, 如果管理员的登录方式是用户名密码+图形验证码，那么策略表达式为  username_password_code  

--secret-key:  密钥key

--ttl  :  令牌过期时间

verifier： 验证相关配置

--header ：头信息相关配置  ，用户需要将登录类型 和登录令牌以头的方式传给后端。

--ignore-url ：忽略地址列表

--privilege-url ：特权地址列表

--privilege-user  ：特权用户列表

### 微服务架构

#### 认证微服务

（1）在需要提供登录功能的微服务上，添加依赖

```xml
<dependency>
    <groupId>cn.elegent.security</groupId>
    <artifactId>elegent-security-token</artifactId>
    <version>1.1.0</version>
</dependency>
```

（2）添加配置

```yaml
elegent:
  security:
    login-strategies:
      - type: admin
        strategy: username_password
        secret-key: elegent
        ttl: 2
```

#### 微服务网关

（1）引入依赖

```xml

<dependency>
    <groupId>cn.elegent.security</groupId>
    <artifactId>elegent-security-verifier-gateway</artifactId>
    <version>1.1.0</version>
</dependency>
```

（2）添加配置，例子如下：

```yaml
elegent:
  security:
    login-strategies:
      - type: admin
        secret-key: elegent
    verifier:
      header:
        type: login-type
        token: user-token
      ignore-url:  # 忽略地址，不用登录也可以访问的地址
        - POST/login
        - POST/logout
      privilege-url: #特权地址，所有登录用户都可以访问的地址
        - GET/user/list
        - GET/goods/list
      privilege-user:
        - admin
```



#### 其它微服务

其它的微服务，因为需要获取当前登录用户，需要引入上下文依赖

```xml
<dependency>
    <groupId>cn.elegent.security</groupId>
    <artifactId>elegent-security-context</artifactId>
    <version>1.1.0</version>
</dependency>
```



## 代码开发

### 定义用户服务类

用户服务类作用是根据用户名可以查询用户信息。框架中提供了用户服务类接口

```java
/**
 * 用户认证
 */
public interface UserDetailsServices {


    /**
     * 根据用户名加载用户信息
     * param username
     * param type
     * return
     */
    UserDetails loadUserByUsername(String username, String type);

}
```

loadUserByUsername方法就是仅根据用户名以及类型，查询用户信息，封装到UserDetails类中。

框架提供了一个默认的实现类，其中定义了两个用户test 和admin ，密码均为123456 ， 用于简单案例的测试。

所以项目在做了框架集成后，就自动会有admin 和 test 两个用户。

开发者需要自己创建一个实现了UserDetailsServices接口的实现类，来定义用户的查询和封装逻辑。

```java
@Component
public class UserDetailServiceImpl implements UserDetailsServices {

    @Override
    public UserDetails loadUserByUsername(String username, String type) {
        //todo: 根据username和type查询数据库，然后封装到UserDetails中
    }
}
```

这样系统就会根据你编写的逻辑，实现查询用户逻辑。

测试：

POST http://localhost:8080/login

```json
{
"username":"user",
"password":"123456",
"type":"admin"
}
```

返回内容：

```json
{
    "tokenDetails": {
        "type": "admin",
        "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ1c2VyIiwiaWF0IjoxNjk0ODU1NTEyLCJwYXNzd29yZCI6IiQyYSQxMCRFakRwTVp1bFhuVFFvb25hTUsvdWN1WWljR0hFWko0VkZEY1BSYUZmWGRBcC9EblVkcW51LiIsInJvbGVzIjpbIlRFU1QiXSwicmVzb3VyY2VzIjpbIkdFVC90ZXN0Il0sImVuYWJsZWQiOnRydWUsInN1cGVyVXNlciI6ZmFsc2UsInVzZXJuYW1lIjoidXNlciIsImV4cCI6MTY5NDg2MjcxMn0.tlopU9blXr-exGPOSNuC85NgMZ47rslOQbRQYR4KorY",
        "refreshToken": null
    },
    "userDetails": {
        "username": "user",
        "password": null,
        "roles": [
            "TEST"
        ],
        "resources": [
            "GET/test"
        ],
        "enabled": true,
        "superUser": false
    },
    "errorInfo": null,
    "authenticated": true
}
```

### 图形验证码

框架提供了图形验证码的访问接口

http://localhost:8080/user/imageCode/{clientToken}

clientToken 是前端传递的用于区分每个用户的图形验证码的字符串

如果你要使用图形验证码登录验证，策略表达式为  username_password_code

完整的配置：

```yaml
elegent:
  security:
    login-strategies:
      - type: admin
        strategy: username_password_code
        secret-key: elegent
        ttl: 2
    verifier:
      header:
        type: login-type
        token: user-token
      ignore-url:  # 忽略地址，不用登录也可以访问的地址
        - POST/login
        - POST/logout
      privilege-url: #特权地址，所有登录用户都可以访问的地址
        - GET/user/list
        - GET/goods/list
      privilege-user:
        - admin

```

登录测试：

POST http://localhost:8080/login

```json
{
"username":"user",
"password":"123456",
"type":"admin",
"clientToken":"12345",
"code":"n2fn"
}
```

### 短信验证码

框架提供了短信验证码的访问接口

http://localhost:8080/user/code/{mobile}

系统没有提供发送短信的功能，生成的短信验证码会在控制台输出

用户需要自行实现发送短信验证码的逻辑，编写类实现 SmsService 接口

如果你要使用手机号验证码方式登录，策略表达式 为 mobile_code

完整的配置：

```yaml
elegent:
  security:
    login-strategies:
      - type: admin
        strategy: username_password_code
        secret-key: elegent
        ttl: 2
      - type: user
        strategy: mobile_code
        secret-key: elegent
        ttl: 2
    verifier:
      header:
        type: login-type
        token: user-token
      ignore-url:  # 忽略地址，不用登录也可以访问的地址
        - POST/login
        - POST/logout
      privilege-url: #特权地址，所有登录用户都可以访问的地址
        - GET/user/list
        - GET/goods/list
      privilege-user:
        - admin
```

### 双令牌刷新

我们可以通过配置，实现令牌刷新

```yaml
elegent:
  security:
    login-strategies:
      - type: admin
        strategy: username_password_code
        secret-key: elegent
        ttl: 2
        refresh-key: refreshElegent  ## 刷新令牌key
        refresh-ttl: 200  ## 刷新令牌过期时间
```

测试登录结果如下格式：

```json
{
    "tokenDetails": {
        "type": "admin",
        "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ1c2VyIiwiaWF0IjoxNjk0ODY2NzE1LCJwYXNzd29yZCI6IiQyYSQxMCRGMC40dHUxUU9TZ1ZuOVpHYUNYTU1lRndOajhmT0RCeVc4Wk1lNjNJblNJcHlJNlRLbEt4cSIsInJvbGVzIjpbIlRFU1QiXSwicmVzb3VyY2VzIjpbIkdFVC90ZXN0Il0sImVuYWJsZWQiOnRydWUsInN1cGVyVXNlciI6ZmFsc2UsInVzZXJuYW1lIjoidXNlciIsImV4cCI6MTY5NDg3MzkxNX0.Louu97lVK4rrdPE4ttgsrDtayA2sbcwVAe12V1cEJpU",
        "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ1c2VyIiwiaWF0IjoxNjk0ODY2NzE2LCJwYXNzd29yZCI6IiQyYSQxMCRGMC40dHUxUU9TZ1ZuOVpHYUNYTU1lRndOajhmT0RCeVc4Wk1lNjNJblNJcHlJNlRLbEt4cSIsInJvbGVzIjpbIlRFU1QiXSwicmVzb3VyY2VzIjpbIkdFVC90ZXN0Il0sImVuYWJsZWQiOnRydWUsInN1cGVyVXNlciI6ZmFsc2UsInVzZXJuYW1lIjoidXNlciIsImV4cCI6MTY5NTU4NjcxNn0.dI8COIi_dqyiu6mQ-8kydeOSkedIoWe9AjO6dXi2RDE"
    },
    "userDetails": {
        "username": "user",
        "password": null,
        "roles": [
            "TEST"
        ],
        "resources": [
            "GET/test"
        ],
        "enabled": true,
        "superUser": false
    },
    "errorInfo": null,
    "authenticated": true
}
```

refreshToken就是刷新令牌

当访问令牌过期后，我们可以通过刷新令牌再次获得最新的访问令牌和刷新令牌

POST  http://localhost:8080/refresh/{type}

```json
{
"refreshToken":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ1c2VyIiwiaWF0IjoxNjk0ODY2NzE2LCJwYXNzd29yZCI6IiQyYSQxMCRGMC40dHUxUU9TZ1ZuOVpHYUNYTU1lRndOajhmT0RCeVc4Wk1lNjNJblNJcHlJNlRLbEt4cSIsInJvbGVzIjpbIlRFU1QiXSwicmVzb3VyY2VzIjpbIkdFVC90ZXN0Il0sImVuYWJsZWQiOnRydWUsInN1cGVyVXNlciI6ZmFsc2UsInVzZXJuYW1lIjoidXNlciIsImV4cCI6MTY5NTU4NjcxNn0.dI8COIi_dqyiu6mQ-8kydeOSkedIoWe9AjO6dXi2RDE"
}
```

得到最新的令牌

```json
{
    "type": "admin",
    "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ1c2VyIiwiaWF0IjoxNjk0ODY3MTM2LCJwYXNzd29yZCI6IiQyYSQxMCRGMC40dHUxUU9TZ1ZuOVpHYUNYTU1lRndOajhmT0RCeVc4Wk1lNjNJblNJcHlJNlRLbEt4cSIsInJvbGVzIjpbIlRFU1QiXSwicmVzb3VyY2VzIjpbIkdFVC90ZXN0Il0sImVuYWJsZWQiOnRydWUsInN1cGVyVXNlciI6ZmFsc2UsInVzZXJuYW1lIjoidXNlciIsImV4cCI6MTY5NDg3NDMzNn0.-vYyGVBKSiWatYWRVPhcneKjNj8pNKIWs5INg4660yU",
    "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ1c2VyIiwiaWF0IjoxNjk0ODY3MTM2LCJwYXNzd29yZCI6IiQyYSQxMCRGMC40dHUxUU9TZ1ZuOVpHYUNYTU1lRndOajhmT0RCeVc4Wk1lNjNJblNJcHlJNlRLbEt4cSIsInJvbGVzIjpbIlRFU1QiXSwicmVzb3VyY2VzIjpbIkdFVC90ZXN0Il0sImVuYWJsZWQiOnRydWUsInN1cGVyVXNlciI6ZmFsc2UsInVzZXJuYW1lIjoidXNlciIsImV4cCI6MTY5NTU4NzEzNn0.OgtgQ3mVQlhE4AFss2U83ZRFihxIk0HW_D7Eetez0Xk"
}
```



### 对方法的权限控制

如果你想要在项目中，使用注解对方法进行权限控制，按以下步骤即可：

（1）引入依赖

```xml
<dependency>
    <groupId>cn.elegent.security</groupId>
    <artifactId>elegent-security-annotation</artifactId>
    <version>1.1.0</version>
</dependency>
```

（2）在需要控制的方法上，添加注解RolesAllowed ，指定角色名称（可以配置多个）

```java
@RolesAllowed("角色名称")
```



## 高级应用

### ElegentSecurity系统架构图

#### 组件架构图

这是整体的组件架构图，展现了用户获取令牌与访问资源两种操作都用到哪些组件。

用户在登录后，由登录入口调用认证管理器，默认的认证管理器会调用用户详情服务获得用户信息，再调用密码编码器对密码进行校验，校验通过后通过令牌构建器创建令牌。

用户在携带令牌访问后端资源的时候，会经过鉴权过滤器，鉴权过滤器会调用鉴权管理器来实现对令牌进行校验，它通过调用令牌获取器获取令牌，通过调用URL获取器获得当前访问资源地址，再通过令牌检查器检查令牌。

![](doc/images/jg.png)



### 自定义组件

#### 自定义密码编码器

框架提供的密码编码器接口如下：

```java
/**
 * 密码编码器
 */
public interface PasswordEncoder {


    /**
     * 匹配密码
     * param rawPassword 原始密码
     * param encodedPassword 编码后密码
     * return
     */
    boolean matches(String rawPassword, String encodedPassword);


    /**
     * 转换密码
     * param rawPassword
     * return
     */
    String encode(CharSequence rawPassword);

}
```

框架提供了PasswordEncoder的实现类BcryptPasswordEncoder，用户可以通过实现PasswordEncoder接口来自定义密码的编码规则。

编写后，在配置类添加配置。

```java
/**
* 密码编码器配置
* return
*/
@Bean
public PasswordEncoder passwordEncoder(){
    return new XXXXXXPasswordEncoder();
}
```

#### 自定义令牌构建器

框架提供的令牌构建器接口如下：

```java
/**
 * 令牌建造者接口
 */
public interface TokenBuilder {

    /**
     * 根据用户详情创建令牌详情
     * param userDetails
     * return
     */
    TokenDetails createToken(UserDetails userDetails);

}
```

框架提供了TokenBuilder的实现类JWTSymmetryTokenBuilder（JWT对称加密构建器），用户可以通过实现TokenBuilder接口来自定义令牌的构建逻辑。

编写后，在配置类添加Bean配置。

#### 自定义令牌获取器

框架提供的令牌获取器接口如下：

```java
/**
 * 令牌获取者
 */
public interface TokenAcquirer<T> {

    /**
     * 获取token
     * return
     */
    TokenDetails getToken(T requestObject);

}
```

框架提供了两个实现 HeaderTokenAcquirer 和  GatewayTokenAcquirer ，HeaderTokenAcquirer用于单体架构下的令牌获取，GatewayTokenAcquirer 用于在微服务架构下网关中令牌的获取。 这里使用泛型是因为两个实现类的方法所用的参数类型不同 ，HeaderTokenAcquirer 用的是HttpServletRequest，而GatewayTokenAcquirer 用的是ServerHttpRequest 。

如果用户想自己实现令牌的获取逻辑，只需要自己写一个实现TokenAcquirer接口的类，泛型类型可以是HttpServletRequest或ServerHttpRequest 。

编写后，在配置类添加Bean配置。

#### 自定义令牌检查器

框架提供的令牌检查器接口如下：

```java
/**
 * 令牌检查器
 */
public interface TokenChecker {

    /**
     * 检查令牌
     * param token
     * return
     */
    UserDetails checkToken(String token);

}
```

框架提供了JWTSymmetryTokenChecker（对称加密的令牌检查），如果想自定义令牌认证的逻辑，可以编写类实现TokenChecker接口，并进行Bean配置。

#### 自定义认证管理器

认证管理器主要职责是签发令牌，框架提供的认证管理器接口如下：

```java
/**
 * 认证管理器接口
 */
public interface AuthenticationManager {

    /**
     * 认证
     * param userAuth
     * return
     */
    AuthenticateResult authenticate(UserAuth userAuth);

}
```

系统提供默认实现DefaultAuthenticationManager，用户可以编写类实现AuthenticationManager接口，并进行bean配置。

#### 自定义鉴权管理器

鉴权管理器的主要职责是检查令牌合法性，检验用户权限。框架提供的鉴权管理器接口如下：

```java
/**
 * 授权管理器
 */
public interface AuthorizationManager<T,M> {

    /**
     * 检查
     * param requestObject
     * param responseObject
     * return
     */
    AuthorizationResult check(T requestObject,M responseObject);
}

```

接口定义的两个泛型，T是请求对象 M是响应对象。

系统提供了默认的鉴权管理器DefaultAuthorizationManager ，如果用户想自定义鉴权逻辑，可以编写类实现AuthorizationManager<T,M>接口，并进行BEAN配置。



## 参与贡献

1.  从 `master` 分支 `checkout` 一个新分支（**注**：*请务必保证 master 代码是最新的*）
2.  新分支命名格式：`docs/username_description`，例如：`docs/tom_新增分布式锁配置项`
3.  在新分支上编辑文档、代码，并提交代码
4.  最后 `PR` 合并到 `develop` 分支，等待作者合并即可


