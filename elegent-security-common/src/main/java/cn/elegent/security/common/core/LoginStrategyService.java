package cn.elegent.security.common.core;

import cn.elegent.security.common.base.LoginStrategy;

import java.util.Map;

public interface LoginStrategyService {

    /**
     * 获取全部的登录策略
     * @return
     */
    Map<String, LoginStrategy> getLoginStrategy();

}
