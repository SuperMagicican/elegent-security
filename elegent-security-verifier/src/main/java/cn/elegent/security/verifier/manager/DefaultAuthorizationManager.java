package cn.elegent.security.verifier.manager;
import cn.elegent.security.common.base.AuthorizationResult;
import cn.elegent.security.common.base.TokenDetails;
import cn.elegent.security.common.base.UserDetails;
import cn.elegent.security.common.constant.CheckResult;
import cn.elegent.security.common.context.SubjectContext;
import cn.elegent.security.verifier.core.*;
import cn.elegent.security.verifier.properties.SecurityProperties;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
public class DefaultAuthorizationManager<T,M> implements AuthorizationManager<T,M> {


    private TokenAcquirer<T> tokenAcquirer;  //令牌获取器

    private UrlAcquirer<T> urlAcquirer;//URL获取器

    private TokenChecker tokenChecker;//令牌检查器

    private SecurityProperties securityProperties; //属性

    private PathMatcher pathMatcher;//地址匹配器


    @Override
    public AuthorizationResult check(T request,M response)  {

        //获取当前url
        String targetUrl = urlAcquirer.getUrl(request);

        //判断是否是忽略地址
        //添加免配项
        securityProperties.getIgnoreUrl().add("POST/**/login");//登录
        securityProperties.getIgnoreUrl().add("POST/**/refresh/**"); //刷新
        securityProperties.getIgnoreUrl().add("GET/**/user/imageCode/**"); //验证码
        securityProperties.getIgnoreUrl().add("GET/**/user/code/**"); //验证码
        boolean isMatch=false;
        try {
            isMatch = securityProperties.getIgnoreUrl()
                    .stream().anyMatch(url -> pathMatcher.match(url, targetUrl));
        }catch (Exception e){
        }

        if(isMatch){
            return  AuthorizationResult.builder()
                    .checkResult(CheckResult.AUTH_OK  ).build();
        }

        //获取token
        TokenDetails tokenDetails = tokenAcquirer.getToken(request);
        if(tokenDetails==null) {
            return  AuthorizationResult.builder()
                    .checkResult(CheckResult.NO_LOGIN  ).build();
        }

        //检查token
        UserDetails userDetails = tokenChecker.checkToken(tokenDetails.getAccessToken(),tokenDetails.getType());
        if(userDetails==null){
            return  AuthorizationResult.builder()
                    .checkResult(CheckResult.NO_LOGIN  ).build();
        }

        //如果是超级用户
        if(userDetails.isSuperUser()){
            return  AuthorizationResult.builder()
                    .checkResult(CheckResult.AUTH_OK  )
                    .userDetails(userDetails)
                    .token(tokenDetails.getAccessToken()  ).build();
        }
        //判断当前用户是否是特权用户
        boolean b1 =false;
        try{
            b1= securityProperties.getPrivilegeUser()
                    .stream()
                    .anyMatch(user -> user.equals(userDetails.getUsername()));
        }catch (Exception e){
        }
        if(b1){
            return  AuthorizationResult.builder()
                    .checkResult(CheckResult.AUTH_OK  )
                    .userDetails(userDetails)
                    .token(tokenDetails.getAccessToken()  ).build();
        }
        //将特权地址 加入到当前用户资源列表
        if(userDetails.getResources()==null){
            userDetails.setResources(new ArrayList<>());
        }

        userDetails.getResources().addAll( securityProperties.getPrivilegeUrl() );

        //判断地址
        boolean b = false;
        try {
            b = userDetails.getResources().stream()
                    .anyMatch(res -> pathMatcher.match(res, targetUrl));
        } catch (Exception e) {
        }
        if(b){
            return  AuthorizationResult.builder()
                    .checkResult(CheckResult.AUTH_OK  )
                    .userDetails(userDetails)
                    .token(tokenDetails.getAccessToken()  ).build();
        }
        //输出访问异常
        return  AuthorizationResult.builder()
                .checkResult(CheckResult.NO_ACCESS  )
                .userDetails(userDetails)
                .token(tokenDetails.getAccessToken()  ).build();
    }
}
