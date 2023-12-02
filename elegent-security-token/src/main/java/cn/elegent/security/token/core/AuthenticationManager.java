package cn.elegent.security.token.core;

import cn.elegent.security.common.base.AuthenticateResult;
import cn.elegent.security.common.base.TokenDetails;
import cn.elegent.security.common.base.UserAuth;

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


    /**
     *  刷新令牌
     * @param refreshToken
     * @return
     */
    TokenDetails refresh(String  refreshToken,String type);
}
