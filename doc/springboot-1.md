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

​	我们需要配置的东西，Spring Boot帮我们自动配置：@**EnableAutoConfiguration**告诉srpingboot开启自动配置功能；这样自动配置才能生效

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

直接写 k: v

​	字符串默认不用加上单引号或者双引号

​	双引号：不会转义字符串里面的特殊字符，特殊字符会作为本身想表示的意思

**对象、Map（属性和值）（键值对）**

k：v在下一行来写对象的属性和值的关系；注意缩进

对象还是k： v的方式

```yaml
friends:
	lastName: Zhang
	age: 20
```

**数组(List、 Set)**

用-值表示数组中的一个元素

```yaml
pets: 
	- cat
	- dog
	- pig
```

## 3、配置文件值注入

yaml配置文件

```yaml
person:
        lastName: zhangsan
        age: 18
        boss: false
        birth: 2017/12/12
        maps: {k1: v1, k2: v2}
        lists:
            - lisi
            - wangwu
            - zhaoliu
        dog:
            name: giggle
            age: 2
```

java bean

```java
/**
 * 将配置文件中配置的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties :告诉Spring Boot将本类中的所有属性和配置文件中相关的配置进行绑定
 * 	perfix = "person" : 配置文件中哪个下面的所有属性进行一一映射
 * 	 只要该组件是容器中的组件，才能使用容器中的功能
 */
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
	private String lastName;
	private Integer age;
	private Boolean boss;
	private Date birth;

	private Map<String, Object> maps;
	private List<Object> lists;
	private Dog dog;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Boolean getBoss() {
		return boss;
	}

	public void setBoss(Boolean boss) {
		this.boss = boss;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Map<String, Object> getMaps() {
		return maps;
	}

	public void setMaps(Map<String, Object> maps) {
		this.maps = maps;
	}

	public List<Object> getLists() {
		return lists;
	}

	public void setLists(List<Object> lists) {
		this.lists = lists;
	}

	public Dog getDog() {
		return dog;
	}

	public void setDog(Dog dog) {
		this.dog = dog;
	}

	@Override
	public String toString() {
		return "Person{" +
				"lastName='" + lastName + '\'' +
				", age=" + age +
				", boss=" + boss +
				", birth=" + birth +
				", maps=" + maps +
				", lists=" + lists +
				", dog=" + dog +
				'}';
	}
}
```

单元测试

```java
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

    @Test
    public void contextLoads() {
        System.out.println(person);
    }

}
```

可以导入配置文件处理器，以后编写配置文件就有提示了

```xml
        <!-- 导入配置文件处理器，配置文件进行绑定就会有提示-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
```

### 1、@Value获取值和@ConfigurationProperties获取值比较 

|                       | @ConfigurationProperties | @Value   |
| --------------------- | ------------------------ | -------- |
| 功能                  | 批量注入配置文件中的属性 | 逐个指定 |
| 松散绑定（松散语法）  | 支持                     | 不支持   |
| SpEL                  | 不支持                   | 支持     |
| JSR303数据校验        | 支持                     | 不支持   |
| 复杂类型封闭（如map） | 支持                     | 不支持   |

配置文件不管是yml还是properties，两种方式都能获取到值；

**如果只是在某个业务逻辑中需要获取一下配置文件中的某项值，就使用@Value；如果专门编写了一个JavaBean来和配置文件进行映射，则使用@ConfigurationProperties**

### 2、配置文件注入值数据校验

```java
@Component
@ConfigurationProperties(prefix = "person")
@Validated
public class Person {
	/**
	 * <bean class="Person">
	 * <property name="lastName" value="字面量/${key}/#{SpEL}从环境变量、配置文件中取值" />
	 * </bean>
	 */
	//lastName必须是email
	@Email
	private String lastName;
	private Integer age;
	private Boolean boss;
	private Date birth;

	private Map<String, Object> maps;
	private List<Object> lists;
	private Dog dog;
}
```

### @PropertiesSource & @ImportResource

@PropertiesSource加载指定的配置文件

```java
/**
 * 	 @ConfigurationProperties(prefix="person)  默认从全局配置文件中获取值
 */
@Component
@ConfigurationProperties(prefix = "person")
@PropertySource(value = {"classpath:person.properties"})
public class Person {
	/**
	 * <bean class="Person">
	 * <property name="lastName" value="字面量/${key}/#{SpEL}从环境变量、配置文件中取值" />
	 * </bean>
	 */
	private String lastName;
	private Integer age;
	private Boolean boss;
	private Date birth;

	private Map<String, Object> maps;
	private List<Object> lists;
	private Dog dog;
}
```

@ImportResource：导入Spring的配置文件，让配置文件里面的内容生效

Spring Boot里面没有Spring的配置文件，我们自己编写的配置文件，也不能自动识别；

如果需要让Spring的配置文件生效，加载进来，需要将@ImportResource加到配置类上

```java
@ImportResource(locations = {"classpath:beans.xml"})
导入spring配置文件使其生效，需要加在Spring配置类上
```



不来Spring配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   
    <bean id="helloService" class="com.meituan.service.HelloService"></bean>

</beans>
```



Spring Boot推荐给容器中添加组件的方式：推荐使用全注解的方式

配置类=========Spring配置文件

 

使用@Bean来给容器添加组件

```
/**
 * 指明当前类是一个配置类，用来替代之前的Spring配置文件
 *  在配置文件中用<bean></bean>标签添加组件
 */
@Configuration
public class MyAppConfig {

    //将方法的返回值添加到容器中，容器中这个组件默认的id就是方法名
    @Bean
    public HelloService helloService() {
        return new HelloService();
    }
}
```



## 4、配置文件点位符

### 1、随机数

```java
${random.value}
${random.int}
${random.int(max)}
${random.int(min,max)}
${random.long}
${random.long(max)}
${random.long(min,max)}
${random.uuid}
```

### 2、占位符获取之前配置的值，如果没有可以指定默认值

```yaml
        dog:
            name: ${person.last-name:hello}_giggle
            age: 2
        last-name: zhangsan${random.uuid}
```

## 5、Profile

### 1、多Profile文件

在主配置文件编写的时候，文件名可以是application-{profile}.properties/yml，实现动态切换；默认使用application.properties/yml中的配置

### 2、yml支持多文档块方式

```yaml
spring:
  profiles:
    active: dev

---
server:
  port: 8081
spring:
  profiles: dev

---
server:
  port: 8082
spring:
  profiles: prod
```



### 3、激活指定profile

1. 在配置文件中指定spring.profiles.active=dev

```yaml
spring:
  profiles:
    active: dev
```

2. 命令行的方式激活

```shell
java -jar springboot.jar --spring.profile.active=dev
```

3. 虚拟机参数

```shell
VM options:
	-Dspring.profiles.active=dev	
```

## 6、配置文件加载位置

Spring Boot启动会扫描以下位置的application.properties或者application.yml文件作为Spring Boot的默认配置文件

 - file:./config/
- file:./
- classpath:/config/
- classpath:/
- 以上是按照优先级从高到低的顺序，所有位置的文件都会被加载，高优先级配置会覆盖低优先级配置内容；Spring Boot会从这个个位置全部加载主配置文件，实现**互补配置**；
- 可以通过spring.config.location来改变默认配置；

其中，file是指当前项目根目录

## 7、外部配置加载顺序

```markdown
1. Devtools global settings properties on your home directory (~/.spring-boot-devtools.properties when devtools is active).
2. @TestPropertySource annotations on your tests.
3. @SpringBootTest#properties annotation attribute on your tests.
4. Command line arguments.
5. Properties from SPRING_APPLICATION_JSON (inline JSON embedded in an environment variable or system property)
6. ServletConfig init parameters.
7. ServletContext init parameters.
8. JNDI attributes from java:comp/env.
9. Java System properties (System.getProperties()).
10. OS environment variables.
11. A RandomValuePropertySource that only has properties in random.

由jar包外向jar包内进行寻找，优先加载带profile
12. **Profile-specific application properties outside of your packaged jar (application-{profile}.properties and YAML variants)
13. Profile-specific application properties packaged inside your jar (application-{profile}.properties and YAML variants)
再加载不带profile
14. Application properties outside of your packaged jar (application.properties and YAML variants).
15. Application properties packaged inside your jar (application.properties and YAML variants).**
16. @PropertySource annotations on your @Configuration classes.
17. Default properties (specified using SpringApplication.setDefaultProperties).
```

## 8、 Spring Boot自动配置原理

### 1、 精髓

[配置文件能配置的属性参照](https://docs.spring.io/spring-boot/docs/1.5.9.RELEASE/reference/html/common-application-properties.html)

**自动配置原理（非常重要）**：

1）SpringBoot启动的时候加载主配置类，开启了自动配置功能

2）@EnableAutoConfiguration作用：

- 利用EnableAutoConfigurationImportSelector给容器中导入一些组件
- List<String> configurations = this.getCandidateConfigurations(annotationMetadata, attributes);

		获取候选的配置

```
SpringFactoriesLoader.loadFactoryNames()
扫描所有jar包类路径下的文件：META-INF/spring.factories
将扫描到的文件内容，包装成Properties对象
从Properties获取到EnableAutoConfiguration.class（类名）对应的值，然后将其添加在容器中
```

​	将类路径下 META-INF/spring.factories 里面配置的所有EnableAutoConfiguration的值加入到容器中。

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration,\
org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration,\
org.springframework.boot.autoconfigure.cloud.CloudAutoConfiguration,\
org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration,\
org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration,\
org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration,\
org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration,\
org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.couchbase.CouchbaseDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.couchbase.CouchbaseRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration,\
org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.ldap.LdapDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.ldap.LdapRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,\
org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration,\
org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration,\
org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration,\
org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration,\
org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration,\
org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration,\
org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration,\
org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration,\
org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration,\
org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration,\
org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration,\
org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration,\
org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration,\
org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,\
org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration,\
org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration,\
org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration,\
org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration,\
org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration,\
org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration,\
org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration,\
org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration,\
org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration,\
org.springframework.boot.autoconfigure.mobile.DeviceResolverAutoConfiguration,\
org.springframework.boot.autoconfigure.mobile.DeviceDelegatingViewResolverAutoConfiguration,\
org.springframework.boot.autoconfigure.mobile.SitePreferenceAutoConfiguration,\
org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration,\
org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,\
org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration,\
org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,\
org.springframework.boot.autoconfigure.reactor.ReactorAutoConfiguration,\
org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration,\
org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration,\
org.springframework.boot.autoconfigure.security.FallbackWebSecurityAutoConfiguration,\
org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration,\
org.springframework.boot.autoconfigure.sendgrid.SendGridAutoConfiguration,\
org.springframework.boot.autoconfigure.session.SessionAutoConfiguration,\
org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration,\
org.springframework.boot.autoconfigure.social.FacebookAutoConfiguration,\
org.springframework.boot.autoconfigure.social.LinkedInAutoConfiguration,\
org.springframework.boot.autoconfigure.social.TwitterAutoConfiguration,\
org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration,\
org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration,\
org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration,\
org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration,\
org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration,\
org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration,\
org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration,\
org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration,\
org.springframework.boot.autoconfigure.web.HttpEncodingAutoConfiguration,\
org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration,\
org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration,\
org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration,\
org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration,\
org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration,\
org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration,\
org.springframework.boot.autoconfigure.websocket.WebSocketMessagingAutoConfiguration,\
org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration
```



3) 每一个自动配置类自动配置

4) 以**HttpEncodingAutoConfiguration**为例解释自动配置原理：

```java
@Configuration //表示这是一个配置类，与以前编写的配置文件一样，可以给容器中添加组件
@EnableConfigurationProperties({HttpEncodingProperties.class})//启动指定类的ConfigurationProperties功能，将配置文件中对应的值和HttpEncodingProperties绑定起来，并加入到IOC容器中
@ConditionalOnWebApplication//Spring底层有@Conditional注解，根据不同的条件，如果满足指定的条件，整个配置类才会生效，当前注解判断是否为Web应用，如果是，当前配置类生效；
@ConditionalOnClass({CharacterEncodingFilter.class})//判断当前项目是否存在CharacterEncodingFilter类，是SpringMVC中乱码解决的过滤器
@ConditionalOnProperty(
    prefix = "spring.http.encoding",
    value = {"enabled"},
    matchIfMissing = true
) //判断配置文件中是否存在某个配置
public class HttpEncodingAutoConfiguration {
    
    //已经跟Spring配置文件映射起来了
    private final HttpEncodingProperties properties;

    //只有一个有参构造器时，参数的值会从容器中拿
    public HttpEncodingAutoConfiguration(HttpEncodingProperties properties) {
        this.properties = properties;
    }
    /**
    * 如果生效，则将下列组件添加到容器中
    */
    @Bean
    @ConditionalOnMissingBean({CharacterEncodingFilter.class})
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
        filter.setEncoding(this.properties.getCharset().name());
        filter.setForceRequestEncoding(this.properties.shouldForce(Type.REQUEST));
        filter.setForceResponseEncoding(this.properties.shouldForce(Type.RESPONSE));
        return filter;
    }
}
```

根据当前不同的条件判断，决定这个配置类是否生效

一旦这个配置类生效，这个配置类就是给容器中添加各种组件；这些组件的属性是从对应的Properties类中获取的，而这些类的中的每一个属性，又是与配置文件绑定的



5) 所有在配置文件中能配置的属性都在xxxxProperties类中封闭着，配置文件能配置什么就可以参照HttpEncodingProperties类

```java
@ConfigurationProperties(
    prefix = "spring.http.encoding"
)
public class HttpEncodingProperties
```

Spring Boot 精髓：

​	1）SpringBoot启动会加载大量的自动配置类

​	2） 需要的功能有没有SpringBoot默认写好的自动配置类

​	3）再考察自动配置类中到底配置了哪些组件，如果已经有就不需要再配置了，如果没有需要自己编写配置类

​	4）给容器中自动配置类添加组件的时候，会从Properties类中获取某些属性，此时可以在配置文件中指定这些属性的值



xxxxAutoConfiguration：自动配置类，给容器添加组件

xxxxProperties：封闭配置文件中的相关属性

### 2、细节

@Conditional 派生注解

| @Conditional扩展注解                 | 作用（判断是否满足当前指定条件） |
| ------------------------------- | ------------------------------------------------------- |
| @ConditionalOnJava              | 系统的java版本是否符合要求                              |
| @ConditionalOnBean              | 容器中存在指定Bean；                                    |
| @ConditionalOnMissingBean       | 容器中不存在指定Bean；                                  |
| @ConditionalOnExpression        | 满足SpEL表达式指定                                      |
| @ConditionalOnClass             | 系统中有指定的类                                        |
| @ConditionalOnMissingClass      | 系统中没有指定的类                                      |
| @ConditionalOnSingleCandidate   | 容器中只有一个指定的Bean，或者这个Bean是首选Bean        |
| @ConditionalOnProperty          | 系统中指定的属性是否有指定的值                          |
| @ConditionalOnResource          | 类路径下是否存在指定资源文件                            |
| @ConditionalOnWebApplication    | 当前是web环境                                           |
| @ConditionalOnNotWebApplication | 当前不是web环境                                         |
| @ConditionalOnJndi              | JNDI存在指定项                                          |

