package cn.elegent.security.verifier.handler;

import cn.elegent.security.verifier.core.AuthenticationEntryPoint;
import cn.elegent.security.verifier.core.TokenAcquirer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认认证异常处理类
 */
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint<HttpServletRequest,HttpServletResponse> {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
