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
 * @SpringBootApplication 来标注一个主程序，说明这是一个Spring Boot应用
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

它来真正管理Spring Boot应用里面的所有依赖版本，可以称之为Spring Boot的版本仲裁中心，以后导入依赖默认不需要写版本。

没有在中心管理的依赖，自然需要声明版本号。

#### 2、导入的依赖

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

spring-boot-starter-web

spring-boot-starter : Spring Boot场景启动器，帮我们导入了web模块正常运行所依赖的组件；

Spring Boot将所有的功能场景都抽取出来，做成一个个的starters（启动器），只需要在项目里引入这些starter，相关场景的所有依赖都会导入进来。需要什么功能就导入什么场景的启动器

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

@**SpringBootApplication**: Spring Boot应用标注在某个类上，说明这个类是Spring Boot的主配置类，Spring Boot应该运行这个类的main方法来启动Spring Boot应用

```
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Configuration
@EnableAutoConfiguration
@ComponentScan
public @interface SpringBootApplication
```

@**Configuration**

配置类上来标注这个注解

配置类----配置文件；配置类也是窗口中的一个组件，@Component

@**EnableAutoConfiguration**

​	我们需要配置的东西，Spring Boot帮我们自动配置：@**EnableAutoConfiguration**告诉srpingboot开户自动配置功能；这样自动配置才能生效

```java
@Target(value=TYPE)
@Retention(value=RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(value=EnableAutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration
```

@**AutoConfigurationPackage**

```
@Target(value=TYPE)
@Retention(value=RUNTIME)
@Documented
@Inherited
@Import(value=org.springframework.boot.autoconfigure.AutoConfigurationPackages.Registrar.class)
public @interface AutoConfigurationPackage
```

 	@**Import**(value=org.springframework.boot.autoconfigure.AutoConfigurationPackages.Registrar.class)

spring 的底层注解@Import，给容器中导入一个组件；导入的组件由AutoConfigurationPackages.Registrar.class管理；作用是<u>将主配置类所在包及下所有子包里面的所有组件扫描到Spring容器中</u>



@Import(value=EnableAutoConfigurationImportSelector.class)

给容器导入组件：EnableAutoConfigurationImportSelector.class，导入哪些组件的选择器；将所有需要导入的组件以全类名的方式返回；这些组件就会被添加到容器中。

有了自动配置类，免去了手动编写配置和注入功能组件等工作



**Spring Boot在启动的时候，从类路径下的META-INFO/spring.factories中获取EnableAutoConfiguration指定的值，将这些值作为自动配置类导入到容器中，自动配置类生效，从而可以进行自动配置工作**；以前需要自己配置的东西，自动配置类代为完成了。

J2EE的整体整合解决方案和自动配置都在spring-boot-autoconfigure-****(此处为版本号).jar

## 6、使用Spring Initializer快速创建Spring Boot应用

IDE都支持使用Spring项目创建向导快速创建一个Spring Boot项目

选择需要的模块，向导会联网创建Spring Boot项目

默认生成的Spring Boot项目：

+ 主程序已经生成好了，只需要添加自己的逻辑
+ resources文件夹中的目录结构
  + static：保存所有的静态资源：js css images
  + templates：保存所有的模板页面（Spring Boot默认jar包使用嵌入式的Tomcat，默认不支持JSP页面）；可以使用模板引擎（freemarker、thymeleaf）；
  + application.properties：Spring Boot应用的配置文件

# 二、Spring Boot配置

## 1、配置文件

Spring Boot使用一个全局的配置文件，配置文件名是固定的：

- application.properties
- application.yml

配置文件的作用：修改Spring Boot自动配置的默认值；Spring是底层都给我们配置好



YAML

标记语言

以前配置文件大多使用xml文件，YAML以数据为中心，比json、xml等更适合做配置文件

配置例子

```
server:
	port: 8081
```

## 2、YAML语法

### 1、 基本语法

key：（空格）value表示一对键值对（必须有空格）

以空格的缩进来控制层级关系；只要是左对齐的一列数据，都是同一层级的

```
server:
	port: 8081
	path: /hello
```

属性和值大小写敏感

### 2、 值的写法

**字面量：普通的值（数字，字符串，布尔）**