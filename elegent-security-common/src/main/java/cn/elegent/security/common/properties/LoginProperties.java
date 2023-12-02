package cn.elegent.security.common.properties;

import cn.elegent.security.common.base.LoginStrategy;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.List;

/**
 * jw配置文件
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "elegent.security")
public class LoginProperties implements Serializable {


    private List<LoginStrategy> loginStrategies;



}
