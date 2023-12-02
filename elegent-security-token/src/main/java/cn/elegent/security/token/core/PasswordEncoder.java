package cn.elegent.security.token.core;

/**
 * 密码编码器
 */
public interface PasswordEncoder {


    /**
     * 匹配密码
     * param rawPassword 原始密码
     * param encodedPassword 编码后密码
     * return 是否匹配
     */
    boolean matches(String rawPassword, String encodedPassword);


    /**
     * 转换密码
     * param rawPassword
     * return
     */
    String encode(CharSequence rawPassword);

}
