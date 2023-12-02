package cn.elegent.security.annotation.core;

import org.aspectj.lang.ProceedingJoinPoint;

public interface ElegentNoAuthResponse {


    public Object noAuthInterview(ProceedingJoinPoint point);


}
