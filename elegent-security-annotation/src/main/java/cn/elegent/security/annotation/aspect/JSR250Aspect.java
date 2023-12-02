package cn.elegent.security.annotation.aspect;

import cn.elegent.security.annotation.core.ElegentNoAuthResponse;
import cn.elegent.security.annotation.util.ReflectionUtil;
import cn.elegent.security.common.base.UserDetails;
import cn.elegent.security.common.context.SubjectContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class JSR250Aspect {

    @Pointcut("@annotation(javax.annotation.security.RolesAllowed)")
    public void RolesAllowed() {
    }


    @Autowired
    private ElegentNoAuthResponse elegentNoAuthResponse;

    /**
     * 权限框架校验
     * param point
     * return
     * throws Throwable
     */
    @Around("RolesAllowed()")
    public Object rolesAllowedAspect(ProceedingJoinPoint point) throws Throwable {

        UserDetails userDetails = SubjectContext.getSubject();
        //如果用户关闭则
        if(!userDetails.isEnabled()){
            return elegentNoAuthResponse.noAuthInterview( point );
        }
        //如果是超级管理员，则放行
        if(userDetails.isSuperUser()){
            return point.proceed( point.getArgs() );
        }
        //获取用户的角色
        List<String> userRoles = userDetails.getRoles();
        if(userRoles==null){
            return elegentNoAuthResponse.noAuthInterview( point );
        }

        //获取方法上的注解
        Method method = ReflectionUtil.getMethodByProceedingJoinPoint(point);
        RolesAllowed rolesAllowed = method.getAnnotation( RolesAllowed.class );
        String[] resourceRoles = rolesAllowed.value();  //获取所有角色

        //如果用户角色中，包含资源规定的角色
        long count = Arrays.stream(resourceRoles).filter(role -> userRoles.contains(role)).count();
        if(count>0) { //执行
            return point.proceed( point.getArgs() );
        }else{
            return elegentNoAuthResponse.noAuthInterview( point );
        }

    }


}