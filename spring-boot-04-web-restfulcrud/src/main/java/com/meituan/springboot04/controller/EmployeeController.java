package com.meituan.springboot04.controller;

import com.meituan.springboot04.dao.EmployeeDao;
import com.meituan.springboot04.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeDao employeeDao;

    //查询所有员工列表页面
    @GetMapping(value = "emps")
    public String list(Model model) {
        /**
         *  thymeleaf默认会拼串
         *
         * @ConfigurationProperties(prefix = "spring.thymeleaf")
         * public class ThymeleafProperties {
         *  private static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");
         *  private static final MimeType DEFAULT_CONTENT_TYPE = MimeType.valueOf("text/html");
         *  public static final String DEFAULT_PREFIX = "classpath:/templates/";
         *  public static final String DEFAULT_SUFFIX = ".html";
         */

        Collection<Employee> employees = employeeDao.getAll();
        model.addAttribute("emps", employees);
        return "emp/list";
    }
}
