package cn.elegent.security.token.vercode;

import cn.elegent.security.token.core.VerCodeService;
import lombok.Builder;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
@Builder
public class EhcacheVerCodeService implements VerCodeService {

    @Autowired
    Cache<String,String> cache;

    @Override
    public void saveCode(String clientToken,String content) {
        cache.put(clientToken, content  );
    }

    @Override
    public String readCode(String clientToken) {
        return cache.get(clientToken );
    }
}
