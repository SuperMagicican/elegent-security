package cn.elegent.security.verifier.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * jw配置文件
 */
@Data
@Configuration
public class HeaderProperties implements Serializable {


    /**
     * 获取请求头的名字( 登录类型)
     */
    @Value("${elegent.security.verifier.header.type:login-type}")
    private String typeHeader;

    /**
     * 获取请求头的名字( 登录类型)
     */
    @Value("${elegent.security.verifier.header.token:user-token}")
    private String tokenHeader;

}
