package cn.elegent.security.token.service;

import cn.elegent.security.common.base.UserDetails;
import cn.elegent.security.token.core.PasswordEncoder;
import cn.elegent.security.token.core.UserDetailsServices;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TestUserDetailService implements UserDetailsServices {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username, String type) {

        if(username.equals("test")){
            UserDetails userDetails=new UserDetails();
            userDetails.setUsername("test");
            String password = passwordEncoder.encode("123456");
            userDetails.setPassword(password);
            userDetails.setEnabled(true);//是否启用
            userDetails.setSuperUser(false);//是否是超级用户

            //可访问的资源地址
            List<String> resources=new ArrayList<>();
            resources.add("GET/test");
            userDetails.setResources(resources);

            //角色列表
            List<String> roles=new ArrayList<>();
            roles.add("TEST");
            userDetails.setRoles(roles);

            return userDetails;
        }
        if(username.equals("admin")){
            UserDetails userDetails=new UserDetails();
            userDetails.setUsername("admin");
            String password = passwordEncoder.encode("123456");
            userDetails.setPassword(password);
            userDetails.setEnabled(true);
            userDetails.setSuperUser(true);//是否是超级用户
            return userDetails;
        }
        return null;

    }
}
