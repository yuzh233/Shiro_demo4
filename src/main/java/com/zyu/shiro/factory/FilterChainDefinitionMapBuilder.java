package com.zyu.shiro.factory;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: z.yu
 * DateTime: 2018-04-22 16:54
 * Description: 用于初始化资源和权限，动态的加载权限，而不是在xml中写死。
 */
@Component
public class FilterChainDefinitionMapBuilder {

    /**
     * 动态的配置资源和权限
     *      /login.jsp = anon
     *      /user/login = anon
     *      /user/logout = logout
     *      /admin.jsp = roles[admin]
     *      /user.jsp = roles[user]
     *      /** = authc
     * @return 链表结构的map，因为shiro存储资源和权限的map是有序的。
     */
    public LinkedHashMap<String, String> buildFilterChainDefinitionMap(){
        // 这个权限应该是从数据库中查找的
        LinkedHashMap<String,String> roles = new LinkedHashMap<>();
        roles.put("/login.jsp","anon");
        roles.put("/user/login","anon");
        roles.put("/user/logout","logout");
        roles.put("/admin.jsp","roles[admin]");
        roles.put("/user.jsp","roles[user]");
        roles.put("/**","authc");
        return roles;
    }
}
