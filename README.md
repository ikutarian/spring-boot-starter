## 来源

SpringBoot开发常用技术整合
：https://www.imooc.com/learn/956

## 构建一个SpringBoot项目

登陆 `http://start.spring.io/` 即可配置一个 Maven 管理的 Spring Boot 项目。只需要按需填写相关信息即可

注意下 pom.xml 文件的内容，一般来说是这样的

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.okada</groupId>
    <artifactId>springboot-starter</artifactId>
    <version>1.0</version>
    <packaging>jar</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.9.RELEASE</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

项目结构是这样

```
└─src
   ├─main
   │  ├─java
   │  │  └─com
   │  │      └─okada
   │  │          └─stater
   │  │              └─ StarterApplication.java
   │  └─resources
   │          application.properties
   │
   └─test
       └─java
```

## 第一个 Controller

创建一个 `HelloController`，访问 `localhost:8080/hello` 是返回一个字符串

```java
package com.okada.stater.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("hello")
    public Object hello() {
        return "Hello SpringBoot~~~";
    }
}
```

## 构造并返回一个 JSON 对象

新建一个用户类 `User`

```java
package com.okada.stater.pojo;

import java.util.Date;

public class User {

    private String name;
    private String password;
    private Integer age;
    private Date birthday;
    private String desc;

    // getter
    // setter
}
```

然后在 `UserController` 中新建一个方法

```java
package com.okada.stater.controller;

import com.okada.stater.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("user")
public class UserController {

    @RequestMapping("getUser")
    @ResponseBody
    public User getUser() {
        User u = new User();
        u.setName("okada");
        u.setAge(18);
        u.setBirthday(new Date());
        u.setPassword("123456");
        u.setDesc(null);

        return u;
    }
}
```

访问 `http://localhost:8080/user/getUser` 即可得到 JSON 数据。

```
{
  "name": "okada", 
  "password": "123456", 
  "age": 18, 
  "birthday": 1532072509566, 
  "desc": null
}
```

一般来说，我们会把 JSON 数据封装一下，变成

```
{
    "status": 200,
    "msg": "ok",
    "data": {
        "id": 1,
        "name": "okada"
    }
}
```

新建一个 JSON 的包装类 `JSONResult`

```java
package com.okada.stater.pojo;

/**
 * 自定义响应数据结构
 *
 * 这个类是提供给门户，ios，安卓，微信商城用的
 *
 * status的含义
 * 200：表示成功
 * 500：表示错误，错误信息在msg字段中
 */
public class JSONResult {

    // 响应状态
    private Integer status;
    // 响应消息
    private String msg;
    // 响应中的数据
    private Object data;

    private JSONResult() {
    }

    public static JSONResult ok(Object data) {
        JSONResult result = new JSONResult();
        result.setStatus(200);
        result.setData(data);
        result.setMsg("ok");
        return result;
    }

    public static JSONResult error(String msg) {
        JSONResult result = new JSONResult();
        result.setStatus(500);
        result.setMsg(msg);
        return result;
    }
    
    // getter
    // setter
}
```

这样 Controller 的方法的返回值就要改为 `JSONResult`

```java
package com.okada.stater.controller;

import com.okada.stater.pojo.JSONResult;
import com.okada.stater.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("user")
public class UserController {
    
    @RequestMapping("getUserJSON")
    @ResponseBody
    public JSONResult getUserJSON() {
        User u = new User();
        u.setName("okada");
        u.setAge(18);
        u.setBirthday(new Date());
        u.setPassword("123456");
        u.setDesc(null);

        return JSONResult.ok(u);
    }
}
```

返回

```
{
  "status": 200, 
  "msg": "ok", 
  "data": {
    "name": "okada", 
    "password": "123456", 
    "age": 18, 
    "birthday": 1532072304575, 
    "desc": null
  }
}
```

## Jackson 对 JSON 数据的处理

常用的配置有三种：

* `JsonIgnore`：忽略字段，不在 JSON 中显示
* `JsonFormat`：时间格式化
* `JsonInclude`：对空值的处理

下面这段 JSON

```
{
  "status": 200, 
  "msg": "ok", 
  "data": {
    "name": "okada", 
    "password": "123456", 
    "age": 18, 
    "birthday": 1532072304575, 
    "desc": null
  }
}
```

把敏感信息 `password` 给显示出来了，时间没有格式化而是采用时间戳，`desc` 字段是 `null`，我不想让它显示。所以可以配置以下 `User` 类

```java
package com.okada.stater.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

public class User {

    private String name;
    @JsonIgnore
    private String password;
    private Integer age;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date birthday;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String desc;

    // getter
    // setter   
}
```

现在输出结果如下

```
{
  "status": 200, 
  "msg": "ok", 
  "data": {
    "name": "okada", 
    "age": 18, 
    "birthday": "2018-07-20 15:55:03"
  }
}
```

## 资源文件属性配置

也即是说，通过配置，可以把配置文件中的配置内容映射到实体类。不需要手动地去解析配置文件，然后把配置文件手动地映射到实体类。

首先引入依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

比如在 resources 文件夹下有一个配置文件 resource.properties

```
com.okada.opensource.name=okada
com.okada.opensource.website=www.okada.com
com.okada.opensource.language=Java
```

然后是实体类和注解

```java
package com.okada.stater.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix="com.okada.opensource")
@PropertySource(value="classpath:resource.properties")
public class Resources {

    private String name;
    private String website;
    private String language;

    // getter
    // setter
}
```

在 Controller 中使用

```java
    @Autowired
    private Resources resources;

    @RequestMapping("getResources")
    @ResponseBody
    public JSONResult getResources() {
        Resources copy = new Resources();
        BeanUtils.copyProperties(resources, copy);
        return JSONResult.ok(copy);
    }
```

返回

```
{
  "status": 200, 
  "msg": "ok", 
  "data": {
    "name": "okada", 
    "website": "www.okada.com", 
    "language": "Java"
  }
}
```

## 服务器和Tomcat配置

常用的有：

* 端口
* ContextPath
* URI 编码

打开 `resources` 文件夹下的 `application.properties` 文件，填入

```
server.port=9527
server.context-path=/okada
server.tomcat.uri-encoding=UTF-8
```

现在要访问 `/getUser` 的话，需要输入 `http://localhost:9527/okada/user/getUser`

## 整合模板引擎 —— freemarker

引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
```

在 `application.properties` 中设置 freemarker 的相关配置，比如：文件路径、关闭缓存、字符编码、后缀等

```
############################
#
# freemarker静态资源配置
#
############################
spring.freemarker.template-loader-path=classpath:/templates
# 关闭缓存，即时刷新，上线生产环境需要改为true
spring.freemarker.cache=false
spring.freemarker.charset=UTF-8
spring.freemarker.check-template-location=true
spring.freemarker.content-type=text/html
spring.freemarker.expose-request-attributes=true
spring.freemarker.expose-session-attributes=true
spring.freemarker.request-context-attribute=request
spring.freemarker.suffix=.ftl
```

然后写两个页面测试一下。在 `resources/templates/freemarker` 文件夹下新建两个页面：`center/center.ftl` 和 `index.ftl`

`center/center.ftl`

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Center</title>
</head>
<body>
Freemarker模板引擎
<h1>Center page</h1>
</body>
</html>
```

`index.ftl`

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Center</title>
</head>
<body>
Freemarker模板引擎
<h1>${resource.name}</h1>
<h1>${resource.website}</h1>
<h1>${resource.language}</h1>
</body>
</html>
```

然后是 Controller

```java
package com.okada.stater.controller;

import com.okada.stater.pojo.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("ftl")
public class FreemarkerController {

    @Autowired
    private Resource resource;

    @RequestMapping("index")
    public String index(ModelMap map) {
        map.addAttribute("resource", resource);
        return "freemarker/index";
    }

    @RequestMapping("center")
    public String center() {
        return "freemarker/center/center";
    }
}
```

访问 http://localhost:9527/okada/ftl/center.ftl 和 http://localhost:9527/okada/ftl/index 即可看到页面了

## 整合模板引擎 —— thymeleaf

引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

然后是配置

```
############################
#
# thymeleaf静态资源配置
#
############################
# 不要忘了最后的 / ，否则会找不到路径
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML5
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.content-type=text/html
# 关闭缓存，即时刷新，上线生产环境需要改为true
spring.thymeleaf.cache=false
```

然后写两个页面测试一下。在 resources/templates/thymeleaf 文件夹下新建两个页面：center/center.html 和 index.html

`center/center.html`

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Center</title>
</head>
<body>
Thymeleaf模板引擎
<h1>Center page</h1>
</body>
</html>
```

`index.html`

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Center</title>
</head>
<body>
Thymeleaf模板引擎
<h1 th:text="${name}">hello world~~~~~~~~</h1>
</body>
</html>
```

然后是 Controller

```java
package com.okada.stater.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("th")
public class ThymeleafController {

    @RequestMapping("index")
    public String index(ModelMap map) {
        map.addAttribute("name", "thymeleaf-okada");
        return "thymeleaf/index";
    }

    @RequestMapping("center")
    public String center() {
        return "thymeleaf/center/center";
    }
}
```

分别访问 `http://localhost:9527/okada/th/index` 和 `http://localhost:9527/okada/th/center` 就可以看到页面了

## 全局异常捕获——三种形式

总共有三种形式：

* 页面跳转形式
* ajax形式
* 统一返回异常的形式

## 全局异常捕获——页面跳转形式

有一个 Controller，其中的方法会产生除 0 异常

```java
package com.okada.stater.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("err")
public class ErrorController {

    @RequestMapping("error")
    public String error() {
        int a = 1 / 0;
        return "thymeleaf/error";
    }
}
```

如果没有增加异常处理器，会返回给浏览器 HTTP 500 代码和错误信息。要对异常进行处理，只需要添加一个全局异常处理器即可

```java
package com.okada.stater.exception;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_VIEW = "error";

    @ExceptionHandler(Exception.class)
    public Object errorHandler(HttpServletRequest request, 
                          HttpServletResponse response, Exception e) {
        e.printStackTrace();

        ModelAndView mv = new ModelAndView();
        mv.addObject("exception", e);
        mv.addObject("url", request.getRequestURL());
        mv.setViewName(ERROR_VIEW);

        return mv;
    }
}
```

当产生异常时，处理器就会捕获异常，并返回异常信息页面。异常信息页面内容放在 `resources/template` 文件夹下的 `error.html` 文件中

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>捕获全局异常</title>
</head>
<body>
    <h1 style="color: red;">发生错误：</h1>
    <div th:text="${url}"></div>
    <div th:text="${exception.message}"></div>
</body>
</html>
```

## 全局异常捕获——ajax形式

Controller 抛出一个异常

```
@RequestMapping("ajaxError")
public JSONResult ajaxError() {
    int a = 1 / 0;
    return JSONResult.ok("ok");
}
```

前端向后端发起了一个 ajax 异步请求

```js
$.ajax({
    url: 'http://localhost:9527/okada/err/ajaxError',
    type: 'POST',
    success: function(data) {
        if (data.status == 200) {
            console.log('ok');
        } else {
            console.log('发生异常：' + data.msg);
        }
    },
    error: function(resp, options, throwError) {
        console.log('error');
    }
});
```

全局异常处理器可以这么写

```java
package com.okada.stater.exception;

import com.okada.stater.pojo.JSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public JSONResult errorHandler(HttpServletRequest request, 
                         HttpServletResponse response, Exception e) {
        e.printStackTrace();

        return JSONResult.error(e.getMessage());
    }
}
```

## 全局异常捕获——同时兼容 Web 和 ajax

**核心思路**：只需要判断前端发过来的请求是 ajax 请求还是页面请求即可

发起 ajax 请求的时候，会在 HTTP Request Headers 中添加一个 `X-Requested-With:XMLHttpRequest` 字段，可以利用这个字段来判断是否是 ajax 请求

```java
private boolean isAjax(HttpServletRequest request) {
    return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
}
```

修改之后的全局异常处理器

```java
package com.okada.stater.exception;

import com.okada.stater.pojo.JSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_VIEW = "error";

    @ExceptionHandler(Exception.class)
    public Object errorHandler(HttpServletRequest request, 
                      HttpServletResponse response, Exception e) {
        e.printStackTrace();

        if (isAjax(request)) {
            return JSONResult.error(e.getMessage());
        } else {
            ModelAndView mv = new ModelAndView();
            mv.addObject("exception", e);
            mv.addObject("url", request.getRequestURL());
            mv.setViewName(ERROR_VIEW);

            return mv;
        }
    }

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
```

## 整合 Mybatis——使用 Mybatis-generator 生成 Mapper 和 POJO
## 整合 Mybatis——实现 CRUD 功能
## 整合 Mybatis——使用 Mybatis-pagehelper 实现分页
## 整合 Mybatis——如何自定义 Mapper

