package com.meituan.springboot04.controller;

import com.meituan.springboot04.dao.DepartmentDao;
import com.meituan.springboot04.dao.EmployeeDao;
import com.meituan.springboot04.entities.Department;
import com.meituan.springboot04.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    DepartmentDao departmentDao;

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

    //来到添加员工页面
    @GetMapping(value = "/emp")
    public String toAddPage(Model model) {
        //来到添加页面，查出所有的部门，在页面显示
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("depts", departments);
        return "emp/add";
    }

    /**
     *员工添加
     * SpringMVC自动将请求参数和对象的属性进行一一绑定
     * 要求：
     * 语法参数的名字和JavaBean入参对象里面的属性名是一样的
     */

    @PostMapping(value = "/emp")
    public String addEmp(Employee employee) {
        //来到员工列表页面

        System.out.println("保存的员工信息" + employee);
        //保存员工
        employeeDao.save(employee);
        // redirect:表示重定向到一个地址
        // forward 表示转发到一个地址
        return "redirect:/emps"; // "/"表示当前项目路径
    }

    /**
     * 来到修改页面，查出当前员工，在页面回显
     */
    @GetMapping(value = "/emp/{id}")
    public String toEditPage(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeDao.get(id);
        model.addAttribute("emp", employee);

        //页面要显示所有的部门列表
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("depts", departments);
        //回到修改页面（add页面是一个修改页面二合一页面）
        return "emp/add";
    }

    /**
     * 员工修改;需要提交员工id
     */
    @PutMapping("/emp")
    public String updateEmployee(Employee employee, BindingResult bindingResult) {
        System.out.println("修改员工信息:" + employee);
        employeeDao.save(employee);
        return "redirect:/emps";
    }

    /**
     * 员工删除
     *
     * @return
     */
    @DeleteMapping("/emp/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id) {
        employeeDao.delete(id);
        return "redirect:/emps";
    }
}
