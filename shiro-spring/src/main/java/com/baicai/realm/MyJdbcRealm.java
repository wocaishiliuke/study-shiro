package com.baicai.realm;

import com.baicai.pojo.User;
import com.baicai.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

/**
 * 自定义Realm
 */
public class MyJdbcRealm extends AuthorizingRealm {

    {
        //设置自定义Realm的名称
        super.setName("myJdbcRealm");
    }

    @Autowired
    private UserService userService;

    /** 获取角色、权限源数据 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1.从主体Token中获取用户名
        String username = (String) principals.getPrimaryPrincipal();
        //2.查询用户角色和权限
        List<String> roles = userService.getRolesByUsername(username);
        List<String> permissions = userService.getPermissionsByRoles(roles);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles == null ? null : new HashSet<>(roles));
        info.setStringPermissions(permissions == null ? null : new HashSet<>(permissions));
        return info;
    }

    /** 获取认证源数据 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.从主体Token中获取用户名
        String username = (String) token.getPrincipal();
        //2.通过用户名查询数据库（模拟）
        User exitUser = userService.getUserByUsername(username);
        if (exitUser == null)
            return null;
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, exitUser.getPassword(), this.getName());
        info.setCredentialsSalt(ByteSource.Util.bytes("salt"));//加盐(数据库中的加密密码使用了盐)
        return info;
    }
}