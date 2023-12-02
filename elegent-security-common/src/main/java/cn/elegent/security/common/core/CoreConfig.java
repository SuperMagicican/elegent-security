package cn.elegent.security.common.core;

import cn.elegent.security.common.login.DefaultLoginStrategyService;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Builder
@Data
@Configuration
public class CoreConfig {

    @Bean
    @ConditionalOnMissingBean
    public LoginStrategyService loginStrategyService(){
        return new DefaultLoginStrategyService();
    }


}
