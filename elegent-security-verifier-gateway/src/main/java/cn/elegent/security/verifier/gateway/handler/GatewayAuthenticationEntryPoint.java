package cn.elegent.security.verifier.gateway.handler;

import cn.elegent.security.verifier.core.AuthenticationEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public class GatewayAuthenticationEntryPoint implements AuthenticationEntryPoint<ServerHttpRequest, ServerHttpResponse> {
    @Override
    public void commence(ServerHttpRequest request, ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
    }
}
