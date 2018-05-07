package com.meituan;

import com.meituan.bean.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Spring Boot 单元测试
 *
 * 可以在测试期间很文件的类似编码一样进行自动注入到容器当中
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloWorldMainApplicationTest {

    @Autowired
    Person person;

    @Autowired
    ApplicationContext ioc;

    @Test
    public void testHelloService() {
        boolean b = ioc.containsBean("helloService");
        System.out.println("@Bean给容器添加组件了");
        System.out.println(b);
    }

    @Test
    public void contextLoads() {
        System.out.println(person);
    }

}