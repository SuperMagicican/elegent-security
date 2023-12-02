package cn.elegent.security.verifier.gateway.acquirer;
import cn.elegent.security.common.base.TokenDetails;
import cn.elegent.security.common.core.LoginStrategyService;
import cn.elegent.security.verifier.core.TokenAcquirer;
import cn.elegent.security.verifier.properties.HeaderProperties;
import lombok.Builder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import java.util.List;

@Builder
public class GatewayTokenAcquirer implements TokenAcquirer<ServerHttpRequest> {

    private HeaderProperties headerProperties;

    private LoginStrategyService loginStrategyService;

    @Override
    public TokenDetails getToken(ServerHttpRequest request) {

        TokenDetails tokenDetails=new TokenDetails();

        List<String> headers = request.getHeaders().get(headerProperties.getTokenHeader());
        if(headers==null) return null;
        String token  =headers.get(0);
        if(token==null ) return null;
        tokenDetails.setAccessToken(token);

        List<String> types  = request.getHeaders().get(headerProperties.getTypeHeader());
        String type="default";
        if(types!=null && types.size()>0){
            type= types.get(0);
        }
        tokenDetails.setType(type);

        return tokenDetails;

    }
}
