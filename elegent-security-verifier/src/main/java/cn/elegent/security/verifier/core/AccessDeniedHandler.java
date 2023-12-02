package cn.elegent.security.verifier.core;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问异常处理器
 */
public interface AccessDeniedHandler<T,M> {


    void handle(T request, M response) ;

}
