package cn.elegent.security.token.core;

import cn.elegent.security.common.base.LoginStrategy;
import cn.elegent.security.common.base.TokenDetails;
import cn.elegent.security.common.base.UserDetails;

/**
 * 令牌建造者接口
 */
public interface TokenBuilder {

    /**
     * 根据用户详情创建令牌详情
     * param userDetails
     * return
     */
    TokenDetails createToken(UserDetails userDetails, LoginStrategy loginStrategy);


    /**
     * 刷新令牌
     * param userDetails
     * return
     */
    TokenDetails refreshToken(String accessToken,LoginStrategy loginStrategy);

}
