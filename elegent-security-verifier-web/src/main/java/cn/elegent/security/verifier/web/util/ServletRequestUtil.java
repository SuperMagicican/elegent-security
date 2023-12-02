package cn.elegent.security.verifier.web.util;

import org.apache.tomcat.util.http.MimeHeaders;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Map;

public class ServletRequestUtil {


    public static void addHeader(HttpServletRequest request, Map<String, String> headerMap) {
        if (headerMap==null||headerMap.isEmpty()){
            return;
        }

        Class<? extends HttpServletRequest> c=request.getClass();
        //System.out.println(c.getName());
        System.out.println("request实现类="+c.getName());
        try{
            Field requestField=c.getDeclaredField("request");
            requestField.setAccessible(true);

            Object o=requestField.get(request);
            Field coyoteRequest=o.getClass().getDeclaredField("coyoteRequest");
            coyoteRequest.setAccessible(true);

            Object o2=coyoteRequest.get(o);
            System.out.println("coyoteRequest实现类="+o2.getClass().getName());
            Field headers=o2.getClass().getDeclaredField("headers");
            headers.setAccessible(true);

            org.apache.tomcat.util.http.MimeHeaders m=null;


            MimeHeaders mimeHeaders=(MimeHeaders) headers.get(o2);
            for (Map.Entry<String,String> entry:headerMap.entrySet()){
                mimeHeaders.removeHeader(entry.getKey());
                mimeHeaders.addValue(entry.getKey()).setString(entry.getValue());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
