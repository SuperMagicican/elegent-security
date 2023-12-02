package cn.elegent.security.token.sms;

import cn.elegent.security.token.core.SmsCodeService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class DefaultSmsService implements SmsCodeService {

    @Override
    public boolean sendSmsCode(String mobile, String code) {
        log.info("发送短信: 手机号：{}  验证码： {}",mobile,code);
        return true;
    }
}
