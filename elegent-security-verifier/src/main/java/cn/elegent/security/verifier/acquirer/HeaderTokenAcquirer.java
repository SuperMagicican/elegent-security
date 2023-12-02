package cn.elegent.security.verifier.acquirer;
import cn.elegent.security.common.base.TokenDetails;
import cn.elegent.security.common.core.LoginStrategyService;
import cn.elegent.security.verifier.core.TokenAcquirer;
import cn.elegent.security.verifier.properties.HeaderProperties;
import lombok.Builder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 从请求头获取token
 */
@Builder
public class HeaderTokenAcquirer implements TokenAcquirer<HttpServletRequest> {

    private HeaderProperties headerProperties;

    private LoginStrategyService loginStrategyService;

    @Override
    public TokenDetails getToken(HttpServletRequest request) {

        TokenDetails tokenDetails=new TokenDetails();

        String token  = request.getHeader(headerProperties.getTokenHeader());
        if(token==null ) return null;
        tokenDetails.setAccessToken(token);

        String type= request.getHeader(headerProperties.getTypeHeader());
        if(type==null ){
            type= "default";
        }
        tokenDetails.setType(type);

        return tokenDetails;

    }
}
