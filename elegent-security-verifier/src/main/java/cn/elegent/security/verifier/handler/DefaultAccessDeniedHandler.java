package cn.elegent.security.verifier.handler;

import cn.elegent.security.verifier.core.AccessDeniedHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认访问异常处理器
 */
public class DefaultAccessDeniedHandler implements AccessDeniedHandler<HttpServletRequest,HttpServletResponse> {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)  {
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }
}
