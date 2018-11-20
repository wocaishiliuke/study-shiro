package com.baicai.controller;

import com.baicai.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @PostMapping(value = "/login", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String login(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        token.setRememberMe(user.getRememberMe());
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            return e.getMessage();
        }
        return "登录成功";
    }

    @GetMapping(value = "/testRoleAndPerm", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testRoleAndPerm() {
        Subject subject = SecurityUtils.getSubject();
        StringBuffer sb = new StringBuffer();
        sb.append(subject.hasRole("admin1") ? "" : "没").append("有admin1角色...\n");
        sb.append(subject.hasRole("admin8") ? "" : "没").append("有admin8角色...\n");
        sb.append(subject.isPermitted("user:insert") ? "" : "没").append("有user:insert权限...\n");
        sb.append(subject.isPermitted("user:select") ? "" : "没").append("有user:select权限...\n");
        sb.append(subject.isPermittedAll("user:insert", "user:delete") ? "" : "没").append("有user:insert && user:delete(同时)权限...\n");
        return sb.toString();
    }

    @RequiresRoles(value = "admin1")
    @RequestMapping(value = "/testAnnoRoleAdmin1", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testAnnoRoleAdmin1() { return "有[admin1]角色(anno)..."; }

    @RequiresRoles(value = "admin8")
    @RequestMapping(value = "/testAnnoRoleAdmin8", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testAnnoRoleAdmin8() { return "有[admin8]角色(anno)..."; }

    @RequiresPermissions(value = "user:insert")
    @RequestMapping(value = "/testAnnoPermInsert", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testAnnoPermInsert() { return "有[user:insert]权限(anno)..."; }

    @RequiresPermissions(value = "user:select")
    @RequestMapping(value = "/testAnnoPermSelect", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testAnnoPermSelect() { return "有[user:select]权限(anno)..."; }


    @RequestMapping(value = "/testRole", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testRole() { return "有该角色..."; }

    @RequestMapping(value = "/testRoles", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testRoles() { return "有该些角色..."; }

    @RequestMapping(value = "/testPerm", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testPerm() { return "有该权限..."; }

    @RequestMapping(value = "/testPerms", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testPerms() { return "有该些权限..."; }

    @RequestMapping(value = "/testRolesOr", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testRolesOr() { return "有该些权限中的至少一个..."; }

}
