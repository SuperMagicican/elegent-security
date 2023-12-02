package cn.elegent.security.verifier.gateway.filter;
import cn.elegent.security.common.base.AuthorizationResult;
import cn.elegent.security.common.constant.CheckResult;
import cn.elegent.security.common.constant.ElegentSecurityConstant;
import cn.elegent.security.verifier.core.AccessDeniedHandler;
import cn.elegent.security.verifier.core.AuthenticationEntryPoint;
import cn.elegent.security.verifier.core.AuthorizationManager;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JWT filter
 */
@Component
public class TokenFilter implements GlobalFilter, Ordered {


    @Autowired
    private AuthorizationManager<ServerHttpRequest,ServerHttpResponse> authorizationManager;



    @Autowired
    private AuthenticationEntryPoint<ServerHttpRequest,ServerHttpResponse> authenticationEntryPoint;  //认证异常处理器

    @Autowired
    private AccessDeniedHandler<ServerHttpRequest,ServerHttpResponse> accessDeniedHandler;//访问异常处理器


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();


        AuthorizationResult authorizationResult = authorizationManager.check(request,response);

        if(authorizationResult.getCheckResult()== CheckResult.AUTH_OK){

            try {
                String userDetails = URLEncoder.encode( JSON.toJSONString(authorizationResult.getUserDetails()) , "UTF-8");

                exchange.getRequest().mutate()
                        //==============传递框架必须打包传递的参数=====================
//                        .header(tokenProperties.getHeaderName(),  authorizationResult.getToken())  //令牌
                        .header(ElegentSecurityConstant.RESULT_KEY, userDetails)  //用户信息
                        .build();

                return chain.filter(exchange);

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        if(authorizationResult.getCheckResult()== CheckResult.NO_LOGIN){
            authenticationEntryPoint.commence(request,response);
        }
        if(authorizationResult.getCheckResult()== CheckResult.NO_ACCESS){
            accessDeniedHandler.handle(request,response);
        }

        return exchange.getResponse().setComplete(); //结束处理

    }

    @Override
    public int getOrder() {
        return 0;
    }

}
