package com.baicai.hello;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class HelloShiro {
    public static void main(String[] args) {

        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            System.out.println(true);
        }else {
            System.out.println(false);
        }
    }
}
