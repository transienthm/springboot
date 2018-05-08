# 三、Spring Boot与日志

## 1、日志框架

市面上的日志框架

JUL、JCL、Jboos-logging、logback、log4j、slf4j

| 日志门面（日志的抽象层）          | 日志实现                    |
| --------------------------------- | --------------------------- |
| ~~JCL~~、slf4j、~~jboss-logging~~ | log4j、JUL、log4j2、logback |

左边选择一个facade，右边选择一个实现

日志门面：slf4j

日志实现：logback



Spring Boot：底层是Spring框架，Spring框架默认使用JCL，**SpringBoot选用slf4j+logback**

## 2、slf4j的使用

### 1、如何在系统中使用slf4j

以后开发的时候，日志记录方法的调用，不来直接调用日志的实现类，而是调用日志抽象层里面的方法

给系统里面导入slf4j的jar和logback的实现jar

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    logger.info("Hello World");
  }
}	
```

slf4j图示：![concrete-bindings](https://user-images.githubusercontent.com/16509581/39743814-c6d484ba-52d4-11e8-821d-3343dcca252f.png)

每个日志的实现框架都有息怕配置文件，使用slf4j以后，**配置文件还是做成日志实现框架自己本身的配置文件**

### 2、遗留问题

不同框架底层使用的是不同的日志框架实现，需要统一日志记录

![legacy](https://user-images.githubusercontent.com/16509581/39744296-5ec5d4a8-52d6-11e8-829e-61c666760d19.png)

使用偷天换日包jcl-over-slf4j替换commons-logging，即避免了因缺少依赖时框架报错问题，又替换掉了commons-logging

总结：如何让系统中所有的日志都统一到slf4j？

1. **将系统中其他日志框架先排除出去**
2. **用中间包来替换原有的日志框架，如jcl-over-slf4j.jar**
3. **导入slf4j其他的实现**

## 3、Spring Boot日志关系

SpringBoot使用下列starter管理日志

```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-logging</artifactId>
		</dependency>
```

使用下述依赖将其他日志框架转换为slf4j

```xml
		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>jcl-over-slf4j</artifactId>
		</dependency>
		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>jul-to-slf4j</artifactId>
		</dependency>
		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>log4j-over-slf4j</artifactId>
		</dependency>
```

真实使用的日志框架实现为

```xml
		<dependency>
			<groupId>ch.qos.logback</groupId>
			<artifactId>logback-classic</artifactId>
		</dependency>
```

总结：

1. Spring Boot底层使用slf4j+logback的方式进行日志记录

2. SpringBoot把其他的日志都替换成了slf4j

3. 中间替换包，底层实现时真正使用的是new SLF4JLogFactory();

   ```java
   package org.apache.commons.logging;
   
   import java.util.Hashtable;
   import org.apache.commons.logging.impl.SLF4JLogFactory;
   
   public abstract class LogFactory {
       static String UNSUPPORTED_OPERATION_IN_JCL_OVER_SLF4J = "http://www.slf4j.org/codes.html#unsupported_operation_in_jcl_over_slf4j";
       static LogFactory logFactory = new SLF4JLogFactory();
       ********
       ********
   }
   ```

4. 如果要引入其他框架，一定要把这个框架的默认日志依赖都移除掉。Spring Boot排除掉了Spring框架使用的common-logging

   ```xml
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-core</artifactId>
       <exclusions>
           <exclusion>
               <groupId>commons-logging</groupId>
               <artifactId>commons-logging</artifactId>
           </exclusion>
       </exclusions>
   </dependency>
   ```

   SpringBoot能自动适配所有的日志，而且底层使用slf4j+logback的方式记录日志，引入其他框架时，需要把引入框架所依赖的日志框架排除掉

## 4、日志使用

### 1、默认配置

```yaml
logging:
  level:
    com:
      meituan: trace
  #
  path: F:\workspace\springboot\springboot-logging\log\
  ## 格式化输出日志
  pattern:
    console: 
  # 没有配置path时，在当前项目目录下生成指定日志文件
  #file: springboot.log
```

### 2、指定配置

给类路径下放上每个日志框架自己的配置文件即可

| Logging System          | Customization                                                |
| ----------------------- | ------------------------------------------------------------ |
| Logback                 | `logback-spring.xml`, `logback-spring.groovy`, `logback.xml` or `logback.groovy` |
| Log4j2                  | `log4j2-spring.xml` or `log4j2.xml`                          |
| JDK (Java Util Logging) | `logging.properties`                                         |

Spring Boot推荐使用+spring后缀的配置文件

logback.xml：直接就被日志框架识别了

logback-spring.xml：日志框架就不直接加载日志的配置项，由Spring Boot解析日志配置，从而使用高级功能，例如可以指定开发环境

```xml
<springProfile name="staging">
    <!-- configuration to be enabled when the "staging" profile is active -->
</springProfile>

<springProfile name="dev, staging">
    <!-- configuration to be enabled when the "dev" or "staging" profiles are active -->
</springProfile>

<springProfile name="!production">
    <!-- configuration to be enabled when the "production" profile is not active -->
</springProfile>
```

## 5、 切换日志框架

![legacy](https://user-images.githubusercontent.com/16509581/39744296-5ec5d4a8-52d6-11e8-829e-61c666760d19.png)

按照slf4j的日志适配图，进行相关的切换