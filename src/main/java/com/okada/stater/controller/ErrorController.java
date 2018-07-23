package com.okada.stater.controller;

import com.okada.stater.pojo.JSONResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("err")
public class ErrorController {

    @RequestMapping("error")
    public String error() {
        int a = 1 / 0;
        return "thymeleaf/error";
    }

    @RequestMapping("ajaxError")
    public JSONResult ajaxError() {
        int a = 1 / 0;
        return JSONResult.ok("ok");
    }

    @RequestMapping("ajaxHtml")
    public String center() {
        return "thymeleaf/ajaxHtml";
    }
}
