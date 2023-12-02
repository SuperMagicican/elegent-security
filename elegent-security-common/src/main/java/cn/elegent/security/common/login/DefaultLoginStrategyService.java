package cn.elegent.security.common.login;

import cn.elegent.security.common.base.LoginStrategy;
import cn.elegent.security.common.core.LoginStrategyService;
import cn.elegent.security.common.properties.LoginProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultLoginStrategyService implements LoginStrategyService {

    @Autowired
    private LoginProperties loginProperties;

    @Override
    public Map<String, LoginStrategy> getLoginStrategy() {

        List<LoginStrategy> loginStrategies = loginProperties.getLoginStrategies();
        Map<String,LoginStrategy> map=  loginStrategies.stream().collect(Collectors.toMap( LoginStrategy::getType, Function.identity() ));
        return map;
    }
}
