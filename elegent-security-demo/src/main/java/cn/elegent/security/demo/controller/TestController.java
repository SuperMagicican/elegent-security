package cn.elegent.security.demo.controller;

import cn.elegent.security.common.base.UserDetails;
import cn.elegent.security.common.context.SubjectContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class TestController {

    @RolesAllowed("TEST")
    @GetMapping("test")
    public String test(){
        UserDetails userDetails = SubjectContext.getSubject();
        return "test "+ userDetails.getUsername() ;
    }



    @GetMapping("hello")
    public String hello(){
        UserDetails userDetails = SubjectContext.getSubject();
        return "hello "+ userDetails.getUsername() ;
    }

}
