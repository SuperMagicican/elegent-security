package cn.elegent.security.annotation.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import javax.annotation.security.RolesAllowed;
import java.lang.reflect.Method;

/**
 * 反射工具包
 */
public class ReflectionUtil {



    public static Method getMethodByProceedingJoinPoint(ProceedingJoinPoint point) throws NoSuchMethodException {
        Signature signature = point.getSignature();
        MethodSignature msg=(MethodSignature) signature;
        Method method = null;
        try {
            method = point.getTarget().getClass().getDeclaredMethod(point.getSignature().getName(), msg.getParameterTypes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //通过方法获取DataIdempotent注解
        return method;
    }


}
