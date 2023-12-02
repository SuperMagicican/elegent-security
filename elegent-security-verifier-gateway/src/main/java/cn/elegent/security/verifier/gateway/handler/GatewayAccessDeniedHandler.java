package cn.elegent.security.verifier.gateway.handler;

import cn.elegent.security.verifier.core.AccessDeniedHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public class GatewayAccessDeniedHandler implements AccessDeniedHandler<ServerHttpRequest, ServerHttpResponse> {
    @Override
    public void handle(ServerHttpRequest request, ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
    }
}
