package com.okada.stater.controller;

import com.okada.stater.pojo.JSONResult;
import com.okada.stater.pojo.Resource;
import com.okada.stater.pojo.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("user")
public class UserController {

    @RequestMapping("getUser")
    @ResponseBody
    public User getUser() {
        User u = new User();
        u.setName("okada");
        u.setAge(18);
        u.setBirthday(new Date());
        u.setPassword("123456");
        u.setDesc(null);

        return u;
    }

    @RequestMapping("getUserJSON")
    @ResponseBody
    public JSONResult getUserJSON() {
        User u = new User();
        u.setName("okada");
        u.setAge(18);
        u.setBirthday(new Date());
        u.setPassword("123456");
        u.setDesc(null);

        return JSONResult.ok(u);
    }

    @Autowired
    private Resource resource;

    @RequestMapping("getResources")
    @ResponseBody
    public JSONResult getResource() {
        Resource copy = new Resource();
        BeanUtils.copyProperties(resource, copy);
        return JSONResult.ok(copy);
    }
}
