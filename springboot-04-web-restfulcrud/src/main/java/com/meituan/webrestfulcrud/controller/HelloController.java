package com.meituan.webrestfulcrud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by wangbin on 2018/5/19.
 */
@Controller
public class HelloController {

    //查出一些数据，在页面展示
    @RequestMapping("/success")
    public String success(Map<String, Object> map) {
        map.put("hello", "你好");
        //classpath:/templates/success.html
        return "success";
    }
}
