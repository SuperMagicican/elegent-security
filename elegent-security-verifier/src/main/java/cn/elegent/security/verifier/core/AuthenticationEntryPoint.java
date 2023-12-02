package cn.elegent.security.verifier.core;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 匿名访问异常处理器
 */
public interface AuthenticationEntryPoint<T,M> {


    void commence(T request, M response);

}
