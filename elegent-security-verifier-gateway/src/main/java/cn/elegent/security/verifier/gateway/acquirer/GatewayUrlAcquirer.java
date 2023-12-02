package cn.elegent.security.verifier.gateway.acquirer;
import cn.elegent.security.verifier.core.UrlAcquirer;
import lombok.Builder;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
@Builder
public class GatewayUrlAcquirer implements UrlAcquirer<ServerHttpRequest>{
    @Override
    public String getUrl(ServerHttpRequest request) {

        HttpMethod method = request.getMethod();
        String url = request.getURI().getPath();
        return method.name() + url;

    }
}
