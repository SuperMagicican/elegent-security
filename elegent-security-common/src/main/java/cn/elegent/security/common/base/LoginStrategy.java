package cn.elegent.security.common.base;

import lombok.Data;

/**
 * 登录策略
 */
@Data
public class LoginStrategy {

    private String type;

    private String strategy; //策略规则

    /**
     *  有效时间
     */
    private Integer ttl;

    /**
     * 对称加密密钥
     */
    private String secretKey;

    //private String headerName;

    private Integer refreshTtl; //刷新令牌有效时间

    /**
     * 对称加密密钥（刷新令牌密钥）
     */
    private String refreshKey;

}
