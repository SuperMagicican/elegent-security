package cn.elegent.security.verifier.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * SecurityProperties
 */
@Data
@ConfigurationProperties(prefix = "elegent.security.verifier")
@Configuration
public class SecurityProperties {

    /**
     * 忽略地址
     */
    List<String> ignoreUrl = new ArrayList<>();

    /**
     * 特权地址
     */
    List<String> privilegeUrl = new ArrayList<>();

    /**
     * 超级管理员(特权用户)
     */
    List<String> privilegeUser = new ArrayList<>();

}
