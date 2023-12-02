package cn.elegent.security.verifier.gateway.config;
import cn.elegent.security.common.core.LoginStrategyService;
import cn.elegent.security.verifier.core.*;
import cn.elegent.security.verifier.gateway.acquirer.GatewayTokenAcquirer;
import cn.elegent.security.verifier.gateway.acquirer.GatewayUrlAcquirer;
import cn.elegent.security.verifier.gateway.handler.GatewayAccessDeniedHandler;
import cn.elegent.security.verifier.gateway.handler.GatewayAuthenticationEntryPoint;
import cn.elegent.security.verifier.handler.DefaultAccessDeniedHandler;
import cn.elegent.security.verifier.handler.DefaultAuthenticationEntryPoint;
import cn.elegent.security.verifier.properties.HeaderProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Configuration
public class GatewayVeriferConfigurer {

    @Autowired
    private HeaderProperties headerProperties;

    @Autowired
    private LoginStrategyService loginStrategyService;

    /**
     * 令牌获取器(覆盖默认的令牌获取器)
     * return
     */
    @Bean
    public TokenAcquirer tokenAcquirer(){
        return GatewayTokenAcquirer.builder()
                .loginStrategyService(loginStrategyService)
                .headerProperties(headerProperties).build();
    }


    /**
     * 地址获取器(覆盖默认的地址获取器)
     * return
     */
    @Bean
    public UrlAcquirer urlAcquirer(){
        return GatewayUrlAcquirer.builder()
               .build();
    }

    /**
     * 认证异常处理
     * return
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new GatewayAuthenticationEntryPoint();
    }

    /**
     * 授权访问异常处理
     * return
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new GatewayAccessDeniedHandler();
    }


}
