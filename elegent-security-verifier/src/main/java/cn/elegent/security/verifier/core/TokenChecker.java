package cn.elegent.security.verifier.core;

import cn.elegent.security.common.base.UserDetails;
import io.jsonwebtoken.Claims;

/**
 * 令牌检查器
 */
public interface TokenChecker {

    /**
     * 检查令牌
     * param token
     * return
     */
    UserDetails checkToken(String token,String type);

}
