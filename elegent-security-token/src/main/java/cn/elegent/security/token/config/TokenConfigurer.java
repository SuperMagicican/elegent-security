package cn.elegent.security.token.config;

import cn.elegent.security.token.core.*;
import cn.elegent.security.token.encoder.BcryptPasswordEncoder;
import cn.elegent.security.token.manager.DefaultAuthenticationManager;
import cn.elegent.security.token.service.TestUserDetailService;
import cn.elegent.security.token.sms.DefaultSmsService;
import cn.elegent.security.token.token.JWTSymmetryTokenBuilder;
import cn.elegent.security.token.vercode.EhcacheVerCodeService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.ehcache.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Properties;

@Builder
@Data
@Configuration
@Slf4j
public class TokenConfigurer {




    /**
     * 密码编码器配置
     * return
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder(){
        return new BcryptPasswordEncoder();
    }

    /**
     * 认证管理器
     * return
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManager authenticationManager(){

        return new DefaultAuthenticationManager();
    }

    /**
     * 令牌构建器
     * return
     */
    @Bean
    @ConditionalOnMissingBean
    public TokenBuilder tokenBuilder(){

        return  JWTSymmetryTokenBuilder.builder()
                .build();
    }


    /**
     * 用户服务
     * return
     */
    @Bean
    @ConditionalOnMissingBean
    public UserDetailsServices userDetailsServices(){

        return new TestUserDetailService();
    }

    @Bean
    @ConditionalOnMissingBean
    public Cache cache(){
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        cacheManager.init();
        org.ehcache.Cache<String, String> cache = cacheManager.createCache("elegentCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(100)).build());
        return cache;
    }


    @Bean
    @ConditionalOnMissingBean
    public SmsCodeService smsCodeService(){
        log.info("框架提供的默认短信码服务类已被加载，该服务类只提供控制台输出短信码，如果用户需要真正发送短信，请编写smsCodeService接口的实现类");
        return DefaultSmsService.builder().build();
    }


    @Bean
    @ConditionalOnMissingBean
    public VerCodeService verCodeService(){
        return EhcacheVerCodeService.builder().cache(cache()).build();
    }


    @Bean
    public DefaultKaptcha getDefaultKaptcha() {

        com.google.code.kaptcha.impl.DefaultKaptcha defaultKaptcha = new com.google.code.kaptcha.impl.DefaultKaptcha();
        Properties properties = new Properties();
        // 图片边框
        properties.setProperty("kaptcha.border", "yes");
        // 边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        // 字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        // 图片宽
        properties.setProperty("kaptcha.image.width", "130");
        // 图片高
        properties.setProperty("kaptcha.image.height", "50");
        // 字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "40");
//      // session key
//      properties.setProperty("kaptcha.session.key", "code");
        // 验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 字体
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }

}
