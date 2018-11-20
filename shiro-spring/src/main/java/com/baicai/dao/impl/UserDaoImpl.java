package com.baicai.dao.impl;

import com.baicai.dao.UserDao;
import com.baicai.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    protected static final String AUTHENTICATION_QUERY = "SELECT * FROM users WHERE username = ?";
    protected static final String USER_ROLES_QUERY = "SELECT role_name FROM user_roles WHERE username = ?";
    protected static final String PERMISSIONS_QUERY = "SELECT permission FROM roles_permissions WHERE role_name in (:roleNames)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    /**
     * 根据用户名，查询用户
     * @param username
     * @return
     */
    @Override
    public User getUsersByUsername(String username) {
        System.out.println("从数据库获取认证数据...");
        List<User> users = this.jdbcTemplate.query(AUTHENTICATION_QUERY, new String[]{username}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int i) throws SQLException {
                return new User(rs.getString("username"), rs.getString("password"));
            }
        });
        return CollectionUtils.isEmpty(users) ? null : users.get(0);
    }

    /**
     * 根据用户名，查询用户角色
     * @param username
     * @return
     */
    @Override
    public List<String> getRolesByUsername(String username) {
        System.out.println("从数据库获取角色数据...");
        return this.jdbcTemplate.query(USER_ROLES_QUERY, new String[]{username}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("role_name");
            }
        });
    }

    /**
     * 根据角色名，查询角色权限
     * @param roleNames
     * @return
     */
    @Override
    public List<String> getPermissionsByRoles(List<String> roleNames) {
        System.out.println("从数据库获取权限数据...");
        //jdbcTemplate不支持in，这里使用NamedParameterJdbcTemplate代替
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        Map<String, Object> params = new HashMap<>(4);
        params.put("roleNames", roleNames);
        return namedParameterJdbcTemplate.query(PERMISSIONS_QUERY, params, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("permission");
            }
        });
    }
}
