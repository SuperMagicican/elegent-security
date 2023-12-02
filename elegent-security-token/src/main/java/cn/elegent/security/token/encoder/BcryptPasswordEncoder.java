package cn.elegent.security.token.encoder;

import cn.elegent.security.common.util.BCrypt;
import cn.elegent.security.token.core.PasswordEncoder;

/**
 * Bcrypt密码编码器
 */
public class BcryptPasswordEncoder implements PasswordEncoder {

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return  BCrypt.checkpw(rawPassword,encodedPassword  );
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return BCrypt.hashpw((String) rawPassword, BCrypt.gensalt());
    }

}
