package cn.elegent.security.verifier.config;

import cn.elegent.security.verifier.core.*;
import cn.elegent.security.verifier.manager.DefaultAuthorizationManager;
import cn.elegent.security.verifier.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PathMatcher;

@Configuration
public class ManagerConfigurer {


    @Autowired
    private TokenAcquirer tokenAcquirer;

    @Autowired
    private TokenChecker tokenChecker;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private PathMatcher pathMatcher;


    @Bean
    @ConditionalOnMissingBean
    public AuthorizationManager authorizationManager(){
        //授权管理器
        DefaultAuthorizationManager manager = DefaultAuthorizationManager.builder()
                .tokenAcquirer(tokenAcquirer)
                .tokenChecker(tokenChecker)
                .securityProperties(securityProperties)
                .pathMatcher(pathMatcher)
                .build();

        return manager;
    }


}
