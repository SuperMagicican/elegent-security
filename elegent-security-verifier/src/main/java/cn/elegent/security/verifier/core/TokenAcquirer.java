package cn.elegent.security.verifier.core;

import cn.elegent.security.common.base.TokenDetails;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

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
