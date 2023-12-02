package cn.elegent.security.context.interceptors;

import cn.elegent.security.common.base.UserDetails;
import cn.elegent.security.common.constant.ElegentSecurityConstant;
import cn.elegent.security.common.context.SubjectContext;
import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

public class ElegentSecurityInterceptor implements HandlerInterceptor {


    /**
     * 前置拦截--封装业务数据到ThreadLocal中
     * param request
     * param response
     * param handler
     * return
     * throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String payLoad = request.getHeader(ElegentSecurityConstant.RESULT_KEY);


        if(!StringUtils.isEmpty(payLoad)){
            String decode = URLDecoder.decode(payLoad, "UTF-8");
            UserDetails userDetails = JSON.parseObject(decode, UserDetails.class);
            SubjectContext.setSubject( userDetails );
        }
        return true;
    }

    /**
     * 回收资源
     * param request
     * param response
     * param handler
     * param modelAndView
     * throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        SubjectContext.removeSubject();
    }

    /**
     * 回收资源
     *  request
     *  response
     *  handler
     *  ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SubjectContext.removeSubject();
    }

}
