package cn.elegent.security.common.context;
import cn.elegent.security.common.base.UserDetails;

public class SubjectContext {

    public static ThreadLocal<UserDetails> subjectThreadLocal = new ThreadLocal<UserDetails>() {
        @Override
        protected UserDetails initialValue() {
            return new UserDetails();
        }
    };

    // 提供线程局部变量set方法
    public static void setSubject(UserDetails userDetails) {

        subjectThreadLocal.set(userDetails);
    }
    // 提供线程局部变量get方法
    public static UserDetails getSubject() {

        return subjectThreadLocal.get();
    }
    //清空当前线程，防止内存溢出
    public static void removeSubject() {

        subjectThreadLocal.remove();
    }

}
