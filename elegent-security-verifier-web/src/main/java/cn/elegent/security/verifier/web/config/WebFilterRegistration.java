package cn.elegent.security.verifier.web.config;

import cn.elegent.security.verifier.core.*;
import cn.elegent.security.verifier.manager.DefaultAuthorizationManager;
import cn.elegent.security.verifier.properties.SecurityProperties;
import cn.elegent.security.verifier.web.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PathMatcher;

@Configuration
public class WebFilterRegistration {

    @Autowired
    private AuthorizationManager authorizationManager;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> registrationBean(){

        // 创建FilterRegistrationBean，通过它配置过滤器
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(
                AuthenticationFilter.builder()
                        .authorizationManager(authorizationManager)
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .build()
               ); // 设置过滤器
        registrationBean.addUrlPatterns("/*"); // 设置过滤器生效范围
        registrationBean.setName("AuthenticationFilter"); // 过滤器名称
        registrationBean.setOrder(1); // 设置优先级
        return registrationBean;
    }


}
