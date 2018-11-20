package com.baicai.service.impl;

import com.baicai.dao.UserDao;
import com.baicai.pojo.User;
import com.baicai.service.UserService;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 根据用户名，查询用户
     * @param username
     * @return
     */
    @Override
    public User getUserByUsername(String username) {
        return userDao.getUsersByUsername(username);
    }

    /**
     * 根据用户名，查询用户角色
     * @param username
     * @return
     */
    @Override
    public List<String> getRolesByUsername(String username) {
        if (username == null)
            return null;
        return userDao.getRolesByUsername(username);
    }

    /**
     * 根据角色名，查询角色权限
     * @param roleNames
     * @return
     */
    @Override
    public List<String> getPermissionsByRoles(List<String> roleNames) {
        if (CollectionUtils.isEmpty(roleNames))
            return null;
        return userDao.getPermissionsByRoles(roleNames);
    }

}
