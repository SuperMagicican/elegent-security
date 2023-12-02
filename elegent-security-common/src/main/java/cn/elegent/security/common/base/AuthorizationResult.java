package cn.elegent.security.common.base;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorizationResult {

    private int checkResult;

    private String token;

    private UserDetails userDetails;

}
