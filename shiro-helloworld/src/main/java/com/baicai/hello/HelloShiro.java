package com.baicai.hello;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;

public class HelloShiro {

    public void loginAndAuthorize(String username, String password, String[] roles, String[] permisssions) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //登录认证
        subject.login(token);
        System.out.println("isAuthenticated: " + subject.isAuthenticated());

        //授权（判断是否具备角色、权限）
        subject.checkRoles(roles);
        subject.checkPermissions(permisssions);

        //登出
        subject.logout();
        System.out.println("isAuthenticated after logout: " + subject.isAuthenticated());
    }

    public static void main(String[] args) {
        System.out.println(new Md5Hash("123","salt"));
    }
}
