package com.meituan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication 来标注一个主程序，说明这是一个spring boot应用
 */
@SpringBootApplication
public class HelloWorldMainApplication {
    // spirng 应用启动起来

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloWorldMainApplication.class, args);
    }
}
