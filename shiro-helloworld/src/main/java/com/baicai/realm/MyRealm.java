package com.baicai.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义Realm
 */
public class MyRealm extends AuthorizingRealm {

    Map<String, String> userMap = new HashMap<>(16);
    Set<String> roleSet = new HashSet<>();
    Set<String> permissionSet = new HashSet<>();

    {
        //设置自定义Realm的名称
        super.setName("myRealm");
        //模拟数据库
        userMap.put("tony", "123");
        roleSet.add("admin1");
        roleSet.add("admin2");
        permissionSet.add("user:insert");
        permissionSet.add("user:delete");
    }

    // 模拟数据库查询用户密码
    private String getPasswordByUsername(String username) {
        return userMap.get(username);
    }
    // 模拟数据库查询用户角色
    private Set<String> getRolesByUsername(String username) { return roleSet; }
    // 模拟数据库查询用户权限
    private Set<String> getPermissionsByUsername(String username) { return permissionSet; }

    /** 获取角色、权限源数据 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1.从主体Token中获取用户名
        String username = (String) principals.getPrimaryPrincipal();
        //2.通过用户名查询数据库（模拟）
        Set<String> roles = getRolesByUsername(username);
        Set<String> permissions = getPermissionsByUsername(username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);
        info.setStringPermissions(permissions);
        return info;
    }

    /** 获取认证源数据 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.从主体Token中获取用户名
        String username = (String) token.getPrincipal();
        //2.通过用户名查询数据库（模拟）
        String password = getPasswordByUsername(username);
        if (password == null)
            return null;
        return new SimpleAuthenticationInfo(username, password, this.getName());
    }
}
