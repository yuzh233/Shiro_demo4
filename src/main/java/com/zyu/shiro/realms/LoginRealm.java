package com.zyu.shiro.realms;

import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;

/**
 * Created with IntelliJ IDEA.
 * User: z.yu
 * DateTime: 2018-04-21 10:13
 * Description: 实现认证
 */
public class LoginRealm extends AuthenticatingRealm {

    //用于认证的方法.
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        //1. 把 AuthenticationToken 转换为 UsernamePasswordToken
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        //2. 从 UsernamePasswordToken 中来获取 username
        String username = token.getUsername();

        //3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
        System.out.println("查询到了用户记录。。。");

        //4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
        if ("lisa".equals(username)) { //模拟当用户是lisa时表示用户不存在
            throw new UnknownAccountException("没有找到账户！");
        }

        //5. 根据用户信息的情况, 决定是否需要抛出其他的 AuthenticationException 异常.
        if ("rose".equals(username)) { //模拟当用户是rose时表示用户登陆的其他异常
            throw new AuthenticationException("用户名或密码错误！");
        }
        /**
         *  6. 根据用户的情况, 来构建 AuthenticationInfo 认证信息对象并返回. 通常使用的实现类为: SimpleAuthenticationInfo
         *          principal、credentials是从数据库中获取的.
         *      1). principal: 认证的实体信息. 可以是 username, 也可以是数据表对应的用户的实体类对象.
         *      2). credentials: 密码.（不使用明文密码的话就是加密之后的密码，总之是数据中保存的密码。）
         *      3). 盐值加密
         *           为什么要使用盐值加密？
         *                  当多个用户密码都是123456时，使用MD5加密，就算加密 n 次，加密之后的密码还是一样的。
         *                  盐值加密是在这个密码生成过程中添加一些 "佐料" ，使得就算源密码一样加密之后的密码是唯一的。
         *      4). realmName: 当前 realm 对象的 name. 调用父类的 getName() 方法即可
         */
        Object principal = username; // 认证信息
        Object credentials = null; //数据库中取出。MD5盐值加密之后的密码，这个密码用户自己都不知道，是根据用户唯一标识+原始密码生成得出的。
        if("zhangyu".equals(username)) {
            credentials = "fb4ccc83d9af9c75c948ac7e0c37abf3"; // 用户zhangyu，密码是123456，根据盐值加密之后密码是这个。
        }
        if("cql".equals(username)){
            credentials = "ec5eec877c1ab8b1f2bd5d561d5ee8b5"; // 用户cql，密码是123456，根据盐值加密之后密码是这个。
        }
        ByteSource credentialsSalt = ByteSource.Util.bytes(username); //根据这个唯一的用户名生成盐值
        //生成认证信息
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, getName());
        /**
         * Shiro 是通过 AuthenticatingRealm 的 credentialsMatcher 属性来进行的密码的比对的!
         * 这里可以手动的比对一下，实际开发可以不写
         * @Param  token 包含了前台输入的账户和密码
         * @Param authenticationInfo 包含了数据库查出来的密码，和盐值、用户信息等
         *  Shiro 通过调用 doCredentialsMatch(token, authenticationInfo) 进行密码比对，该方法通过对token对象中的密码加密之后
         *      和authenticationInfo中的密码对比，返回对比结果。
         */
        if(authenticationInfo!=null) {
            boolean result = getCredentialsMatcher().doCredentialsMatch(token, authenticationInfo);
            System.out.println("查看对比的结果" + result);
        }
        return authenticationInfo; //认证成功才会返回认证信息对象
    }

    public static void main(String[] args) {
        //测试盐值加密后密码的值 [ 算法名、原始密码、盐值、加密次数 ]
        String finalPassword = new SimpleHash("MD5", "123456", ByteSource.Util.bytes("cql"), 1024).toString();
        System.out.println(finalPassword);
    }
}