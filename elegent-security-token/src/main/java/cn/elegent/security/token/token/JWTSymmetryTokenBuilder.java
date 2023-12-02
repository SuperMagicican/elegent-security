package cn.elegent.security.token.token;

import cn.elegent.security.common.base.LoginStrategy;
import cn.elegent.security.common.base.TokenDetails;
import cn.elegent.security.common.base.UserDetails;
import cn.elegent.security.common.util.JWTUtils;
import cn.elegent.security.token.core.TokenBuilder;
import com.alibaba.fastjson.JSON;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 对称加密 JWT令牌 构建器
 */
@Builder
public class JWTSymmetryTokenBuilder implements TokenBuilder {


    @Override
    public TokenDetails createToken(UserDetails userDetails, LoginStrategy loginStrategy) {

        Map<String,Object> claims = JSON.parseObject(JSON.toJSONString(userDetails), Map.class);
        TokenDetails tokenDetails=new TokenDetails();
        tokenDetails.setType( loginStrategy.getType() );

        String accessToken = JWTUtils.createJWT(
                userDetails.getUsername(), claims, loginStrategy.getSecretKey(), loginStrategy.getTtl(), TimeUnit.HOURS);

        tokenDetails.setAccessToken(accessToken);

        if(loginStrategy.getRefreshKey()!=null){
            if(loginStrategy.getRefreshTtl()==null){
                loginStrategy.setRefreshTtl( loginStrategy.getTtl()*10 );
            }

            String refreshToken = JWTUtils.createJWT(
                    userDetails.getUsername(), claims, loginStrategy.getRefreshKey(), loginStrategy.getRefreshTtl(), TimeUnit.HOURS);
            tokenDetails.setRefreshToken(refreshToken);
        }

        return tokenDetails;
    }

    @Override
    public TokenDetails refreshToken(String refreshToken,LoginStrategy loginStrategy) {

        Object object = JWTUtils.verifyJWT(refreshToken, loginStrategy.getRefreshKey());
        if(object==null  ) return null;
        UserDetails userDetails = JSON.parseObject(JSON.toJSONString(object), UserDetails.class);
        return createToken(userDetails,loginStrategy);
    }
}
