package cn.elegent.security.common.base;

import lombok.Data;

import java.util.List;

/**
 * 用户查询结果封装
 */
@Data
public class UserDetails {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色列表
     */
    private List<String> roles;


    /**
     * 资源列表
     */
    private List<String> resources;

    /**
     * 是否启用
     */
    private boolean isEnabled;

    /**
     * 是否是超级用户
     */
    private boolean isSuperUser;


}
