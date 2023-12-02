package cn.elegent.security.verifier.config;

import cn.elegent.security.common.core.LoginStrategyService;
import cn.elegent.security.verifier.acquirer.DefaultUrlAcquirer;
import cn.elegent.security.verifier.acquirer.HeaderTokenAcquirer;
import cn.elegent.security.verifier.checker.JWTSymmetryTokenChecker;
import cn.elegent.security.verifier.core.*;
import cn.elegent.security.verifier.handler.DefaultAccessDeniedHandler;
import cn.elegent.security.verifier.handler.DefaultAuthenticationEntryPoint;
import cn.elegent.security.verifier.properties.HeaderProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class VeriferConfigurer {

    @Autowired
    private HeaderProperties headerProperties;

    @Autowired
    private LoginStrategyService loginStrategyService;

    /**
     * 地址获取器(覆盖默认的地址获取器)
     */
    @Bean
    @ConditionalOnMissingBean
    public UrlAcquirer urlAcquirer(){
        return DefaultUrlAcquirer.builder()
                .build();
    }


    /**
     * 令牌获取器
     */
    @Bean
    @ConditionalOnMissingBean
    public TokenAcquirer tokenAcquirer(){
        return HeaderTokenAcquirer.builder()
                .loginStrategyService(loginStrategyService)
                .headerProperties(headerProperties).build();
    }

    /**
     * 令牌检查器
     */
    @Bean
    @ConditionalOnMissingBean
    public TokenChecker tokenChecker(){
        return  JWTSymmetryTokenChecker.builder()
                .loginStrategyService(loginStrategyService).build();
    }

    /**
     * 地址检查器
     */
    @Bean
    @ConditionalOnMissingBean
    public PathMatcher pathMatcher(){
        return new AntPathMatcher();
    }

    /**
     * 认证异常处理
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new DefaultAuthenticationEntryPoint();
    }

    /**
     * 授权访问异常处理
     */
    @Bean
    @ConditionalOnMissingBean
    public AccessDeniedHandler accessDeniedHandler(){
        return new DefaultAccessDeniedHandler();
    }


}
