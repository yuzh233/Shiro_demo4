package com.zyu.shiro.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: z.yu
 * DateTime: 2018-04-22 17:31
 * Description:
 */
@Service
public class UserService {

    //
//    @RequiresRoles({"admin"}) // 需要有管理员角色才能访问，但是Shiro注解最好不要放在service，service在设置事务通知之后生成代理对象，Shiro注解也会生成代理。
    public void test(){
        System.out.println("UserService.test()...");
        Session session = SecurityUtils.getSubject().getSession();
        System.out.println("Service's Session -> "+session.getAttribute("key"));
    }

}
