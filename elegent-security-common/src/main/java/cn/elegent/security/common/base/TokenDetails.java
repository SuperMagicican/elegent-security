package cn.elegent.security.common.base;

import lombok.Data;

/**
 * 令牌详情
 */
@Data
public class TokenDetails {

    private String type;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;


}
