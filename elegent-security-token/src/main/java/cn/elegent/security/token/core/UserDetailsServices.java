package cn.elegent.security.token.core;

import cn.elegent.security.common.base.UserDetails;

/**
 * 用户认证
 */
public interface UserDetailsServices {


    /**
     * 根据用户名加载用户信息
     * param username 用户名
     * param type 类型
     * return 用户对象
     */
    UserDetails loadUserByUsername(String username, String type);


}
