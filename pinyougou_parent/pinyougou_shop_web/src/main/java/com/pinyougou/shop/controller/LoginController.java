package com.pinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {


    @RequestMapping("/name.do")
    public Map name(){
        //获取当前登录对象的用户名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Map map=new HashMap();

        map.put("loginName",name);

        

        return map;
    }
}
