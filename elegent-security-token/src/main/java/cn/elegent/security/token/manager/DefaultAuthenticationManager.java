package cn.elegent.security.token.manager;

import cn.elegent.security.common.base.*;
import cn.elegent.security.common.constant.LoginType;
import cn.elegent.security.common.core.LoginStrategyService;
import cn.elegent.security.token.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultAuthenticationManager implements AuthenticationManager {


    @Autowired
    private UserDetailsServices userDetailsServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenBuilder tokenBuilder;


    @Autowired
    private VerCodeService verCodeService;//验证码服务

    @Autowired
    private LoginStrategyService loginStrategyService;

    @Override
    public AuthenticateResult authenticate(UserAuth userAuth) {

        Map<String, LoginStrategy> loginStrategyMap = loginStrategyService.getLoginStrategy();

        LoginStrategy loginStrategy = loginStrategyMap.get(userAuth.getType());

        if(loginStrategy==null) return null;

        //如果 是用户名密码 + 验证码
        if(loginStrategy.getStrategy().contains(LoginType.CODE) ){
            if(userAuth.getClientToken()==null){
                userAuth.setClientToken( userAuth.getUsername());
            }
            if(userAuth.getClientToken()==null){
                userAuth.setClientToken(userAuth.getMobile());
            }
            String SystemCode = verCodeService.readCode(userAuth.getClientToken());  //系统存储的验证码
            if(SystemCode==null){
                return AuthenticateResult.builder().isAuthenticated(false).errorInfo("请输入验证码").build();// 返回false
            }
            if(!SystemCode.equals( userAuth.getCode() )){  //如果验证码不匹配
                return AuthenticateResult.builder().isAuthenticated(false).errorInfo("验证码错误").build();// 返回false
            }
        }

        UserDetails userDetails=null;
        if(loginStrategy.getStrategy().contains(LoginType.USERNAME)) {
            //调用用户查询
            userDetails = userDetailsServices.loadUserByUsername(userAuth.getUsername(), userAuth.getType());
            if (userDetails == null) {
                return AuthenticateResult.builder().isAuthenticated(false).errorInfo("无此用户").build();// 返回false
            }
        }
        if(loginStrategy.getStrategy().contains(LoginType.PASSWORD)){
            //调用密码校验
            boolean isMatches = passwordEncoder.matches(userAuth.getPassword(), userDetails.getPassword());
            if(!isMatches){
                return AuthenticateResult.builder().isAuthenticated(false).errorInfo("密码不正确").build();// 返回false
            }
        }
        //如果是手机号，校验格式合法，就颁发令牌
        if(loginStrategy.getStrategy().contains(LoginType.MOBILE)){
            String regex = "^1[3-9]\\d{9}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(userAuth.getMobile());
            if(!matcher.matches()){
                return AuthenticateResult.builder().isAuthenticated(false).errorInfo("手机号不正确").build();// 返回false
            }
            if(userDetails==null){
                userDetails=userDetailsServices.loadUserByUsername(userAuth.getMobile(), userAuth.getType());
            }
        }
        if(userDetails==null){
            return AuthenticateResult.builder().isAuthenticated(false).errorInfo("登录失败").build();// 返回false
        }

        //生成令牌
        TokenDetails tokenDetails = tokenBuilder.createToken(userDetails,loginStrategy);

        userDetails.setPassword(null);
        return AuthenticateResult.builder()
                .isAuthenticated(true)
                .tokenDetails(tokenDetails)
                .userDetails(userDetails)
                .build();
    }

    @Override
    public TokenDetails refresh(String refreshToken,String type) {
        Map<String, LoginStrategy> loginStrategyMap = loginStrategyService.getLoginStrategy();

        LoginStrategy loginStrategy = loginStrategyMap.get(type);
        return tokenBuilder.refreshToken(refreshToken,loginStrategy);
    }
}
