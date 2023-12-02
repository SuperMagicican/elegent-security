package cn.elegent.security.common.base;

import lombok.Data;

/**
 * 用户认证封装对象
 */
@Data
public class UserAuth {

    private String username;

    private String mobile;

    private String password;

    private String clientToken;

    private String code; //验证码

    private String type;//登录类型

}
