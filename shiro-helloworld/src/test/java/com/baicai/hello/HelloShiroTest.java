package com.baicai.hello;

import com.alibaba.druid.pool.DruidDataSource;
import com.baicai.realm.MyRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Properties;

import static org.junit.Assert.*;

public class HelloShiroTest {

    @Before
    public void setSecurityManager() throws IOException {
        /**
         * 1.创建SecurityManager环境
         * 2.Subject提交认证、授权
         * 3.SecurityManager调用Authenticator做认证，Authorizer做鉴权
         * 4.Realm提供认证、角色、权限源数据
         */
        DefaultSecurityManager securityManager = new DefaultSecurityManager();

        // 方式一：SimpleAccountRealm
        //SimpleAccountRealm realm = new SimpleAccountRealm();

        // 方式二：IniRealm
        /*realm.addAccount("tony", "123", "admin1", "admin2");//SimpleAccountRealm只能设置到角色role，不能设置权限permission
        IniRealm realm = new IniRealm("classpath:user.ini");*/

        // 方式三：JdbcRealm
        /*JdbcRealm realm = new JdbcRealm();
        DruidDataSource dataSource = new DruidDataSource();
        Properties properties = new Properties();
        properties.load(new BufferedInputStream(new FileInputStream("src/main/resources/druid.properties")));
        dataSource.configFromPropety(properties);
        realm.setDataSource(dataSource);
        //默认false，即不会查询权限源数据
        realm.setPermissionsLookupEnabled(true);
        //设置自定义SQL
        String authenSql = "SELECT password FROM tb_user WHERE user_name = ?";
        realm.setAuthenticationQuery(authenSql);*/

        // 方式四：自定义Realm
        MyRealm realm = new MyRealm();

        securityManager.setRealm(realm);
        SecurityUtils.setSecurityManager(securityManager);

    }

    @Test
    public void testloginAndAuthorize() {
        new HelloShiro().loginAndAuthorize("tony", "123", new String[]{"admin1", "admin2"}, new String[]{"user:insert","user:delete"/*,"user:update"*/});
    }

}