package cn.elegent.security.annotation.noauth;

import cn.elegent.security.annotation.core.ElegentNoAuthResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
@Component
public class DefaultElegentNoAuthResponse implements ElegentNoAuthResponse {

    /**
     * 没有权限时的返回结果
     * return
     */
    @Override
    public Object noAuthInterview(ProceedingJoinPoint point) {
        //获取request和response
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        MethodSignature methodSignature=(MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        Class<?> returnType = method.getReturnType();
        //判断是不是基本数据类型
        boolean primitive = returnType.isPrimitive();
        if(primitive){
            return getDefaultResult(returnType);
        }else {
            return null;
        }
    }

    /**
     * 如果是基本数据类型的返回值 提供一个默认的返回结果
     * param returnType
     * return
     */
    private Object getDefaultResult(Class returnType){
        //如果是基本类型
        if (int.class.equals(returnType)) {
            return -1;
        }
        if(boolean.class.equals(returnType)){
            return false;
        }
        if (long.class.equals(returnType)) {
            return -1l;
        }
        if(byte.class.equals(returnType)){
            return -1;
        }
        if(short.class.equals(returnType)){
            return -1;
        }
        if(float.class.equals(returnType)){
            return -1;
        }
        if(double.class.equals(returnType)){
            return -1;
        }
        if(char.class.equals(returnType)){
            return 'n';
        }
        return null;
    }
}
