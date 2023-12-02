package cn.elegent.security.token.core;

/**
 * 验证码
 */
public interface VerCodeService {

    /**
     * 保存验证码
     *  clientCode
     *  code
     */
    void saveCode(String clientToken,String content);


    /**
     * 读取验证码
     * @param clientToken
     */
    String readCode(String clientToken);


}
