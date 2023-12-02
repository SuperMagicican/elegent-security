package cn.elegent.security.verifier.acquirer;

import cn.elegent.security.verifier.core.UrlAcquirer;
import lombok.Builder;

import javax.servlet.http.HttpServletRequest;
@Builder
public class DefaultUrlAcquirer implements UrlAcquirer<HttpServletRequest> {
    @Override
    public String getUrl(HttpServletRequest request) {

        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String targetUrl= method + requestURI;
        return targetUrl;

    }

}
