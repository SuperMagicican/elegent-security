package cn.elegent.security.token.core;

/**
 * 短信接口
 */
public interface SmsCodeService {


    boolean sendSmsCode(String mobile,String code);

}
