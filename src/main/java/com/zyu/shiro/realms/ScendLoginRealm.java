package com.zyu.shiro.realms;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: z.yu
 * DateTime: 2018-04-21 15:11
 * Description: 第二个认证 Realm
 * 当存在多个数据库时，mySql使用MD5加密方式，Oracle使用SHA1方式加密，这时需要多个 Realm 了。
 */
public class ScendLoginRealm extends AuthorizingRealm {
    //用于认证的方法.
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1. 把 AuthenticationToken 转换为 UsernamePasswordToken
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        //2. 从 UsernamePasswordToken 中来获取 username
        String username = token.getUsername();

        //3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
        System.out.println("ScendLoginRealm --> 查询到了用户记录。。。");

        //4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
        if ("lisa123".equals(username)) { //模拟当用户是lisa123时表示用户不存在
            throw new UnknownAccountException("ScendLoginRealm --> 没有找到账户！");
        }

        //5. 根据用户信息的情况, 决定是否需要抛出其他的 AuthenticationException 异常.
        if ("rose".equals(username)) { //模拟当用户是rose时表示用户登陆的其他异常
            throw new AuthenticationException("ScendLoginRealm --> 用户名或密码错误！");
        }
        Object principal = username; // 认证信息
        Object credentials = null; //数据库中取出。SHA1盐值加密之后的密码，这个密码用户自己都不知道，是根据用户唯一标识+原始密码生成得出的。
        if("zhangyu".equals(username)) {
            credentials = "66e1a0d9e1ba3f06cc2c85401e52a2e8c4101324"; // 用户zhangyu，密码是1234，根据盐值加密之后密码是这个。密码是123456时第一个Realm通过，是1234时该Realm通过。
        }
        if("cql".equals(username)){
            credentials = "8bfdc17bef3ac9b1710cdaa5438b4778a3df76e5"; // 用户cql，密码是1234，根据盐值加密之后密码是这个。密码是123456时第一个Realm通过，是1234时该Realm通过。
        }
        if("lisa".equals(username)){ // lisa在这里可以认证通过，第一个Realm不可以。
            credentials = "d8cf31bd5b516ce974e7b7b954737ed3a156a6fb"; // 用户lisa，密码是1234，根据盐值加密之后密码是这个。密码是123456时第一个Realm通过，是1234时该Realm通过。
        }
        ByteSource credentialsSalt = ByteSource.Util.bytes(username); //根据这个唯一的用户名生成盐值
        //生成认证信息
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, getName());
        //查看对比结果
        if(authenticationInfo!=null) {
            boolean result = getCredentialsMatcher().doCredentialsMatch(token, authenticationInfo);
            System.out.println("ScendLoginRealm -->查看对比的结果" + result);
        }
        return authenticationInfo;
    }

    public static void main(String[] args) {
        //测试盐值加密后密码的值 [ 算法名、原始密码、盐值、加密次数 ]
        String finalPassword = new SimpleHash("SHA1", "1234", ByteSource.Util.bytes("cql"), 1024).toString();
        System.out.println(finalPassword);
    }

    // 用作授权的
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1. 从 PrincipalCollection 中来获取登录用户的信息（认证信息，可以是用户名，可以是用户对象，反正就是登陆用户的信息。）
        Object principal = principalCollection.getPrimaryPrincipal();

        //2. 利用登录的用户的信息来获取当前用户的角色或权限(可能需要查询数据库)
        Set<String> roles = new HashSet<>(); //模拟从数据库中取出的角色列表
        roles.add("user"); // 每个用户都有这个角色
        if("zhangyu".equals(principal)){ // 上面传入的认证信息是字符串，这里通过字符串匹配角色
            roles.add("admin"); // 给zhangyu这个认证用户赋予 admin 角色
        }

        //3. 创建 SimpleAuthorizationInfo, 并设置其 roles 属性.
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(roles);

        //4. 返回 SimpleAuthorizationInfo 对象.
        return authorizationInfo;
    }
}
