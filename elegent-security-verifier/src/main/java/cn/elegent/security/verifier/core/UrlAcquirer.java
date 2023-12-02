package cn.elegent.security.verifier.core;

public interface UrlAcquirer<T> {

    /**
     * 获取token
     * return
     */
    String getUrl(T requestObject);

}
