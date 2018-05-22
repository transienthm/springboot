# 四、Web

## 1. 简介

使用SpringBoot

1. 创建Spring Boot应用，选中我们需要的模块；
2. SpringBoot已经默认将这些场景配置好了，只需要在配置文件中指定少量配置就可以运行起来
3. 自己编写业务代码



自动配置原理？

> xxxAutoConfiguration自动给容器中配置组件
>
> xxxProperties 配置类来封装配置文件的内容

## 2. SpringBoot对静态资源的映射规则

可以设置和资源有关的参数

```
@ConfigurationProperties(
    prefix = "spring.resources",
    ignoreUnknownFields = false
)
public class ResourceProperties implements ResourceLoaderAware 
```



```
public void addResourceHandlers(ResourceHandlerRegistry registry) {
            if (!this.resourceProperties.isAddMappings()) {
                logger.debug("Default resource handling disabled");
            } else {
                Integer cachePeriod = this.resourceProperties.getCachePeriod();
                if (!registry.hasMappingForPattern("/webjars/**")) {
                    this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{"/webjars/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"}).setCachePeriod(cachePeriod));
                }

                String staticPathPattern = this.mvcProperties.getStaticPathPattern();
                if (!registry.hasMappingForPattern(staticPathPattern)) {
                    this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{staticPathPattern}).addResourceLocations(this.resourceProperties.getStaticLocations()).setCachePeriod(cachePeriod));
                }

            }
        	}
```

1. 所有/webjars/** 请求，都去classpath:/META-INF/resources/webjars/ 找资源

   引入资源

   ```
   		<!-- 引入jquery的webjar -->
   		<dependency>
   			<groupId>org.webjars</groupId>
   			<artifactId>jquery</artifactId>
   			<version>3.3.1</version>
   		</dependency>
   ```

   

   webjars：是指以jar的方式导入资源

   <img width="352" alt="wx20180518-205514 2x" src="https://user-images.githubusercontent.com/16509581/40235772-dde046c4-5add-11e8-89a9-12b7e37fd10e.png">

   如访问：http://localhost:8080/webjars/jquery/3.3.1/jquery.js

2. "/**"访问当前项目的任何资源（静态资源的文件夹）

   ```
   "classpath:/META-INF/resources/", "classpath:/resources/",
   "classpath:/static/", 
   "classpath:/public/
   "/"当前项目的根路径
   ```

   注意：目前SpringBoot下的resources是类路径，并非该路径classpath:/resources/

   localhost:8080/abc 默认去静态文件夹里面找abc

3. 欢迎页：静态资源證下所有的index.html，被/**映射

   ```
   		@Bean
   		public WelcomePageHandlerMapping welcomePageHandlerMapping(
   				ResourceProperties resourceProperties) {
   			return new WelcomePageHandlerMapping(resourceProperties.getWelcomePage(),
   					this.mvcProperties.getStaticPathPattern());
   		}
   ```

4. 所有的**/favicon.ico都是在静态资源文件夹中找

   ```java
   		@Configuration
   		@ConditionalOnProperty(value = "spring.mvc.favicon.enabled", matchIfMissing = true)
   		public static class FaviconConfiguration {
   
   			private final ResourceProperties resourceProperties;
   
   			public FaviconConfiguration(ResourceProperties resourceProperties) {
   				this.resourceProperties = resourceProperties;
   			}
   
   			@Bean
   			public SimpleUrlHandlerMapping faviconHandlerMapping() {
   				SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
   				mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
   				mapping.setUrlMap(Collections.singletonMap("**/favicon.ico",
   						faviconRequestHandler()));
   				return mapping;
   			}
   
   			@Bean
   			public ResourceHttpRequestHandler faviconRequestHandler() {
   				ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
   				requestHandler
   						.setLocations(this.resourceProperties.getFaviconLocations());
   				return requestHandler;
   			}
   
   		}
   ```

5. 自定义静态资源目录

```properties
spring.resources.static-locations=classpath:/hello/,classpath:/meituan/
```

## 3. 模块引擎

JSP、Welocity、Freemarker、Thymeleaf

![template engine](https://user-images.githubusercontent.com/16509581/40268706-d0d4ddd8-5ba4-11e8-8a24-1b834ebb2fd1.png)

Spring Boot推荐使用的模块引擎Thymeleaf

### 3.1 引入Thymeleaf

```xml
		<!-- 引入thymeleaf-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
```

使用Thymeleaf3

```xml
<properties>
    <thymeleaf.version>3.0.2.RELEASE</thymeleaf.version>
    <thymeleaf-layout-dialect.version>2.1.1</thymeleaf-layout-dialect.version>
</properties>
```

增加thymeleaf-layout支持

```xml
        <!--布局功能的支持程序 thymeleaf3主程序 layout2以上版本-->
        <thymeleaf-layout-dialect.version>2.2.2</thymeleaf-layout-dialect.version>
```

### 3.2 使用thymeleaf

```java
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {

	private static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");

	private static final MimeType DEFAULT_CONTENT_TYPE = MimeType.valueOf("text/html");

//只要我们把HTML页面放在classpath:/templates/,thymeleaf就可以自动渲染了
	public static final String DEFAULT_PREFIX = "classpath:/templates/";

	public static final String DEFAULT_SUFFIX = ".html";
	
```

只要我们把HTML页面放在classpath:/templates/,thymeleaf就可以自动渲染了

1. 导入thymeleaf的名称空间

```html
<html xmlns:th="http://www.thymeleaf.org">
```

2. 使用thymeleaf语法

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>success</title>
</head>
<body>
    <h1>成功！</h1>
    <!--将div中的文本内容设置为文本内指定的值-->
    <div th:text="${hello}">这是显示欢迎信息</div>
</body>
</html>
```

### 3.3 语法规则

1. th:text：改变当前元素里面的文本内容

   th: 做生意html属性，来替换原生属性的值

   ![thymeleaf](https://user-images.githubusercontent.com/16509581/40296156-78531628-5d0e-11e8-8b87-721e8fff14b3.png)

2. 表达式语法

   ```properties
   Simple expressions:
   	Variable Expressions: ${...}
   		1. 获取对象的属性、调用方法
   		2. 使用内置的基本对象
   		3. 内置工具对象
   	Selection Variable Expressions: *{...} 选择表达式
   		补充：配合th:object使用
   		<div th:object="${session.user}">
               <p>Name: <span th:text="*{firstName}">Sebastian</span>.</p>
               <p>Surname: <span th:text="*{lastName}">Pepper</span>.</p>
               <p>Nationality: <span th:text="*{nationality}">Saturn</span>.</p>
           </div>
           
   	Message Expressions: #{...} 获取国际化内容
   	
   	Link URL Expressions: @{...} 定义URL链接
   	
   	Fragment Expressions: ~{...}
   	
   Literals 字面量
       Text literals: 'one text' , 'Another one!' ,…
       Number literals: 0 , 34 , 3.0 , 12.3 ,…
       Boolean literals: true , false
       Null literal: null
       Literal tokens: one , sometext , main ,…
   Text operations: 文本操作
       String concatenation: +
       Literal substitutions: |The name is ${name}|
   Arithmetic operations: 数学运算
       Binary operators: + , - , * , / , %
       Minus sign (unary operator): -
   Boolean operations: 布尔运算
       Binary operators: and , or
       Boolean negation (unary operator): ! , not
   Comparisons and equality: 比较运算
       Comparators: > , < , >= , <= ( gt , lt , ge , le )
       Equality operators: == , != ( eq , ne )
   Conditional operators: 条件运算（三元运算）
       If-then: (if) ? (then)
       If-then-else: (if) ? (then) : (else)
       Default: (value) ?: (defaultvalue)
   Special tokens:
       Page 17 of 104
       No-Operation: _
   ```

   

## 4. SpringBoot对SpringMVC的自动配置

### 4.1 SpringBoot 对静态资源的映射规则 

> Spring Boot provides auto-configuration for Spring MVC that works well with most applications.
>
> The auto-configuration adds the following features on top of Spring’s defaults:
>
> - Inclusion of `ContentNegotiatingViewResolver` and `BeanNameViewResolver` beans.
>   - 自动配置了ViewResolver(视图解析器：根据方法的返回值得到视图对象View，视图对象决定如何渲染，转发或重定向)
>   - `ContentNegotiatingViewResolver`组合所有的视图解析器
>   - 如何定制：可以自己给容器中添加一个视图解析器，`ContentNegotiatingViewResolver`会自动将其组合起来
> - Support for serving static resources, including support for WebJars (see below).静态资源文件夹路径
> - Automatic registration of `Converter`, `GenericConverter`, `Formatter` beans.
>   - Converter 转换器 页面接收的数据类型转换使用Converter
>   - Formatter 格式化器，例如2018.5.22=》Date
>   - 自己添加的Convertor、Formattor只需放在容器中即可
> - Support for `HttpMessageConverters` (see below).
>   - `HttpMessageConverter`SpringMVC用来转换Http的请求和响应：User《=》json
>   - `HttpMessageConverters`从容器中确定；获取所有的HttpmessageConverter
>   - 自己给容器中添加HttpMessageConverter，只需将自己的组件注册到容器中(@Bean @Component)
> - Automatic registration of `MessageCodesResolver` (see below).定义错误代码生成规则
> - Static `index.html` support.静态首页访问
> - Custom `Favicon` support (see below).
> - Automatic use of a `ConfigurableWebBindingInitializer` bean (see below).
>   - 可以配置一个`ConfigurableWebBindingInitializer`来替换默认的(添加的容器中)
>   - `ConfigurableWebBindingInitializer`的作用是初始化WebDataBinder
>
> **org.springframework.boot.autoconfigure.web : web的所有自动配置场景**
>
> If you want to keep Spring Boot MVC features, and you just want to add additional [MVC configuration](https://docs.spring.io/spring/docs/4.3.13.RELEASE/spring-framework-reference/htmlsingle#mvc) (interceptors, formatters, view controllers etc.) you can add your own `@Configuration` class of type `WebMvcConfigurerAdapter`, but **without** `@EnableWebMvc`. If you wish to provide custom instances of `RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter` or `ExceptionHandlerExceptionResolver` you can declare a `WebMvcRegistrationsAdapter` instance providing such components.
>
> If you want to take complete control of Spring MVC, you can add your own `@Configuration` annotated with `@EnableWebMvc`.

### 4.2 扩展SpringMVC

```xml
    <mvc:view-controller path="/hello" view-name="success" />
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/hello"/>
            <bean></bean>
        </mvc:interceptor>
    </mvc:interceptors>
```

**编写一个配置类(@Configuration)，是WebMvcConfigurerAdapter类型，不能标注@EnableWebMvc**

既保留了所有的自动配置，也能使用自定义的扩展配置

```java
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/meituan").setViewName("success");

    }
}
```

原理：

1. WebMvcAutoConfiguration是SpringMVC的自动配置类；
2. 在做其他自动配置时，会导入@Import({WebMvcAutoConfiguration.EnableWebMvcConfiguration.class})

```java
@Configuration
    public static class EnableWebMvcConfiguration extends DelegatingWebMvcConfiguration
    
    
    @Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {

	private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();


	@Autowired(required = false)
	public void setConfigurers(List<WebMvcConfigurer> configurers) {
		if (!CollectionUtils.isEmpty(configurers)) {
			this.configurers.addWebMvcConfigurers(configurers);
			//一个参考实现,将所有的WebMvcConfigurer相关配置得了来一起调用
			/**	@Override
                    public void addViewControllers(ViewControllerRegistry registry) {
                        for (WebMvcConfigurer delegate : this.delegates) {
                            delegate.addViewControllers(registry);
                        }
                    }
                    */
		}
	}
```

3. 容器中所有的WebMvcConfigurer都会共同起作用

4. 自定义的配置类也会被调用

   效果：SpringMVC的自动配置和自定义的扩展配置得了会起作用。

### 4.3 全面接管SpringMVC

SpringBoot对SpringMVC的自动配置不需要了，所有都手动配置

此时需要在配置类中添加@EnableWebMvc，SpringBoot对SpringMVC的自动配置都失效

原理：

1. 

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DelegatingWebMvcConfiguration.class)
public @interface EnableWebMvc 
```

2. 

```java
@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport
```

3. WebMvcAutoConfiguration要求没有WebMvcConfigurationSupport注解的类存在在容器中，而添加了@EnableWebMvc的注解恰恰是个WebMvcConfigurationSupport的子类

```java
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurerAdapter.class})
@ConditionalOnMissingBean({WebMvcConfigurationSupport.class})
@AutoConfigureOrder(-2147483638)
@AutoConfigureAfter({DispatcherServletAutoConfiguration.class, ValidationAutoConfiguration.class})
public class WebMvcAutoConfiguration
```

4. 导入的WebMvcConfigurationSupport只是SpringMVC的基本功能

## 5. 如何修改Spring Boot的默认配置

模式：

1. SpringBoot在自动配置很多组件的时候，先看容器中有没有用户自己配置的，如果有就用用户配置的；如果没有才自动配置；如果有些组件可能有多个(ViewResolver)，SpringBoot会将用户配置的和自己默认配置的组合起来
2. 在SpringBoot中会有非常多的xxxConfigurer帮助我们进行扩展配置

## 6. RestfulCRUD

