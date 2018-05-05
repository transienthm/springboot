# 一、Spring  Boot入门

## １、 Srping Boot简介

> 简化Spring应用开发的一个框架
>
> 整个Spring技术栈的一个大整合
>
> J2EE开发的一站式解决方案

## 2、微服务

martin fowler 总结说，微服务是一种架构风格

一个应用应该是一组小型服务；可以通过HTTP的方式进行互通

每一个功能元素最终都是一个可独立替换和独立升级的软件单元。



与之对应的是**单体服务**

详细参照：

> https://martinfowler.com/microservices/

## 3、环境准备

### 1、maven配置 

在maven的settings.xml配置文件的profiles标签添加配置

```xml
    <profile>
        <id>jdk-1.8</id>
        <activation>
            <activeByDefault>true</activeByDefault>
            <jdk>1.8</jdk>
        </activation>
        <properties>
            <maven.compiler.source>1.8</maven.compiler.source>
            <maven.compiler.target>1.8</maven.compiler.target>
            <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
        </properties>
    </profile>
```

### 2、IDEA设置

maven的相关配置

## 4、Spring Boot HelloWorld

一个功能：

浏览器发送Hello请求，服务器接受请求并处理，响应Hello World字符串

### 1、 创建一个maven工程（jar）

### 2、导入相关依赖

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.1.RELEASE</version>
</parent>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

### 3、编写一个主程序

```java
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
```

### 4、创建Controller类

```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }
}
```



### 5、启动服务

```
mvn spring-boot:run
```



### 6、打成一个可执行的jar包

pom.xml中增加，内容

```xml
    <build>
        <plugins>
            <!-- 这个插件，可以将应用打包成一个可执行的jar包 -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

## 5、Hello World探究

### 1、POM文件

#### 1、父项目

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.9.RELEASE</version>
    </parent>
```

上述父项目的父项目是

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <version>1.5.9.RELEASE</version>
    <relativePath>../../spring-boot-dependencies</relativePath>
</parent>

```

它来真正管理springboot应用里面的所有依赖版本，可以称之为springboot的版本仲裁中心，以后导入依赖默认不需要写版本。

没有在中心管理的依赖，自然需要声明版本号。

#### 2、导入的依赖

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

spring-boot-starter-web

spring-boot-starter : springboot场景启动器，帮我们导入了web模块正常运行所依赖的组件；

Springboot将所有的功能场景都抽取出来，做成一个个的starters（启动器），只需要在项目里引入这些starter，相关场景的所有依赖都会导入进来。需要什么功能就导入什么场景的启动器

### 2、主程序类、主入口类

```java
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
```

@**SpringBootApplication**: springboot应用标注在某个类上，说明这个类是springboot的主配置类，springboot应该运行这个类的main方法来启动springboot应用

```
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
public @interface SpringBootApplication
```

@**SpringBootConfiguration**



