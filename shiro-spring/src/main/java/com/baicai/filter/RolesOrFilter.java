package com.baicai.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义角色过滤器，满足一个Role即可访问
 */
public class RolesOrFilter extends AuthorizationFilter {


    /**
     * @param servletRequest
     * @param servletResponse
     * @param o 角色数组
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = this.getSubject(servletRequest, servletResponse);
        String[] roles = (String[]) o;
        if (roles == null || roles.length == 0)
            return true;
        for (String role : roles) {
            //有一个就可以访问
            if (subject.hasRole(role))
                return true;
        }
        return false;
    }
}
