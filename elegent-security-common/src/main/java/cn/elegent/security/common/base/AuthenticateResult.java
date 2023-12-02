package cn.elegent.security.common.base;

import lombok.Builder;
import lombok.Data;

/**
 * 认证结果
 */
@Data
@Builder
public class AuthenticateResult {


    /**
     * 令牌详情对象
     */
    private TokenDetails tokenDetails;

    /**
     * 用户详情对象
     */
    private UserDetails userDetails;

    /**
     * 是否通过认证
     */
    private boolean isAuthenticated;


    /**
     * 错误信息
     */
    private String errorInfo;


}
