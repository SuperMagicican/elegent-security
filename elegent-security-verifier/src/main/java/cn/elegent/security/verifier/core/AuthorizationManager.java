package cn.elegent.security.verifier.core;

import cn.elegent.security.common.base.AuthenticateResult;
import cn.elegent.security.common.base.AuthorizationResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权管理器
 */
public interface AuthorizationManager<T,M> {

    /**
     * 检查
     * param requestObject 请求对象
     * param responseObject 响应对象
     * return 认证结果
     */
    AuthorizationResult check(T requestObject,M responseObject);
}
