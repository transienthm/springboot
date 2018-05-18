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

   如访问：http://localhost:8080/webjars/jquery/3.3.1/jquery.js

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

