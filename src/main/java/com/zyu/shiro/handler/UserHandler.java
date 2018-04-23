package com.zyu.shiro.handler;

import com.zyu.shiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: z.yu
 * DateTime: 2018-04-21 10:15
 * Description:
 */
@Controller
@RequestMapping("/user")
public class UserHandler {
    @Autowired
    private UserService userService;

    private static final transient Logger log = LoggerFactory.getLogger(UserHandler.class);

    @RequestMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        //获取当前的subject
        Subject subject = SecurityUtils.getSubject();
        //测试当前的用户是否已经被认证. 即是否已经登录
        if (!subject.isAuthenticated()) {
            //没有登陆,则把用户名和密码封装为 UsernamePasswordToken 对象
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            token.setRememberMe(true);
            try {
                //执行登录，调用自定义的 Realm 实现类
                subject.login(token);
            } catch (AuthenticationException ae) {
                // 所有认证时异常的父类.
                log.info("登陆失败！"+ae.getLocalizedMessage());
            }
        }
        return "redirect:/list.jsp"; // 还必须是重定向了
    }

    @RequestMapping("/roleAnnoation")
    @RequiresRoles({"admin"}) // 需要有管理员角色才能访问
//    @RequiresAuthentication // 表示认证过才能访问
    public String roleAnnotation(HttpSession session) throws AuthorizationException{
        try {
            //在控制层放入一个 session 属性，测试service层访问这个session（Shiro的Session）
            session.setAttribute("key","this is value");
            userService.test();
        } catch (Exception e) {
            throw new AuthorizationException("没有操作权限~");
        }
        return "welcome";
    }

    @ExceptionHandler(AuthorizationException.class)
    public ModelAndView exceptionPage(Exception e){
        ModelAndView modelAndView = new ModelAndView("unauthorized");
        modelAndView.addObject("error",e.getMessage());
        return modelAndView;
    }

}
