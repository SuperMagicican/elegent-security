package cn.elegent.security.verifier.checker;

import cn.elegent.security.common.base.LoginStrategy;
import cn.elegent.security.common.base.UserDetails;
import cn.elegent.security.common.core.LoginStrategyService;
import cn.elegent.security.common.util.JWTUtils;
import cn.elegent.security.verifier.core.TokenChecker;
import com.alibaba.fastjson.JSON;
import lombok.Builder;

import java.util.Map;

/**
 * 对称加密令牌  检查器
 */
@Builder
public class JWTSymmetryTokenChecker implements TokenChecker {


    private LoginStrategyService loginStrategyService;

    @Override
    public UserDetails checkToken(String token,String type) {


        Map<String, LoginStrategy> map = loginStrategyService.getLoginStrategy();
        if(type==null){
            type="default";
        }
        LoginStrategy loginStrategy = map.get(type);

        Object object = JWTUtils.verifyJWT(token, loginStrategy.getSecretKey());
        if(object==null  ) return null;
        UserDetails userDetails = JSON.parseObject(JSON.toJSONString(object), UserDetails.class);
        return userDetails;
    }
}
