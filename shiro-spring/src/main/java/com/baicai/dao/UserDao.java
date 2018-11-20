package com.baicai.dao;

import com.baicai.pojo.User;

import java.util.List;

public interface UserDao {

    /**
     * 根据用户名，查询用户
     * @param username
     * @return
     */
    User getUsersByUsername(String username);

    /**
     * 根据用户名，查询用户角色
     * @param username
     * @return
     */
    List<String> getRolesByUsername(String username);

    /**
     * 根据角色名，查询角色权限
     * @param roleNames
     * @return
     */
    List<String> getPermissionsByRoles(List<String> roleNames);
}
