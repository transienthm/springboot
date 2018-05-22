package com.meituan.springboot04.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
/*
    @RequestMapping({"/", "/index.html"})
    public String index() {
        return "index";
    }*/


    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {

        return "Hello world!";
    }
}
