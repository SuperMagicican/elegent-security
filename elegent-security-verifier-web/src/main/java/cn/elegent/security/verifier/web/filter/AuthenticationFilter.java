package cn.elegent.security.verifier.web.filter;

import cn.elegent.security.common.base.AuthorizationResult;
import cn.elegent.security.common.constant.CheckResult;
import cn.elegent.security.common.constant.ElegentSecurityConstant;
import cn.elegent.security.verifier.core.AccessDeniedHandler;
import cn.elegent.security.verifier.core.AuthenticationEntryPoint;
import cn.elegent.security.verifier.core.AuthorizationManager;
import cn.elegent.security.verifier.core.TokenAcquirer;
import cn.elegent.security.verifier.web.util.ServletRequestUtil;
import com.alibaba.fastjson.JSON;
import lombok.Builder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 鉴权过滤器
 */
@Builder
public class AuthenticationFilter implements Filter {

    private AuthorizationManager<HttpServletRequest,HttpServletResponse> authorizationManager;

    private TokenAcquirer tokenAcquirer;

    private AuthenticationEntryPoint<HttpServletRequest,HttpServletResponse> authenticationEntryPoint;  //认证异常处理器

    private AccessDeniedHandler<HttpServletRequest,HttpServletResponse> accessDeniedHandler;//访问异常处理器



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response= (HttpServletResponse)servletResponse;

        //调用授权处理器
        AuthorizationResult authorizationResult = authorizationManager.check(request,response);

        if(authorizationResult.getCheckResult()== CheckResult.AUTH_OK){

            if( authorizationResult.getUserDetails()!=null ){
                String userDetails = URLEncoder.encode( JSON.toJSONString(authorizationResult.getUserDetails()) , "UTF-8");
                Map headerMap=new HashMap();
                headerMap.put(ElegentSecurityConstant.RESULT_KEY, userDetails );
                ServletRequestUtil.addHeader( request, headerMap  );
            }
            filterChain.doFilter(servletRequest,servletResponse);  // 放行到下一个过滤器
        }

        if(authorizationResult.getCheckResult()== CheckResult.NO_LOGIN){
            authenticationEntryPoint.commence(request,response);
        }
        if(authorizationResult.getCheckResult()== CheckResult.NO_ACCESS){
            accessDeniedHandler.handle(request,response);
        }

    }



}
