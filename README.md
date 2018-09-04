## 一个简单MVC框架

框架名称是 `SakaMichi`，这是一个日文的罗马拼音，汉字写作“坂道”。走在坂道上，只有不断攀登才能看到更好的风景。

## 使用方法

三个步骤：

1. 定义控制器

```java
public class IndexController {

    public void index(Request request, Response response){
        request.attr("name", "okada");
        response.view("hello");
    }

    public void json(Request request, Response response){
        Map<String, Object> json = new HashMap<>();
        json.put("code", 0);
        json.put("msg", "ok");
        json.put("data", "ojbk");
        response.json(json);
    }
}
```

2. 实现 `Bootstrap` 接口，定义路由

```java
public class App implements Bootstrap {

    @Override
    public void init(SakaMichi sakaMichi) {
        IndexController indexController = new IndexController();
        sakaMichi.addRoute("/", "index", indexController);
        sakaMichi.addRoute("/json", "json", indexController);
    }
}
```

3. 添加过滤器

在 `web.xml` 中添加过滤器

```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1" metadata-complete="true">

    <filter>
        <filter-name>sakamichi</filter-name>
        <filter-class>com.okada.sakamichi.SakaMichiFilter</filter-class>
        <init-param>
            <param-name>bootstrap</param-name>
            <param-value>com.okada.sakamichi.App</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>sakamichi</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

</web-app>
```

## 框架是怎么搭建起来的

对于这个框架我会从以下几个方面来讲解

1. 项目规划
3. 设计思路
2. 路由设计
3. 控制器设计
4. 视图设计
5. 数据库操作

## 项目规划

约定包名：`com.okada.sakamichi`，使用了以下依赖

* JDK 1.8
* Servlet 3.1
* Logback 1.2.3
* jackson 2.9.6

`pom.xml` 文件的内容如下

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.okada</groupId>
    <artifactId>sakamichi</artifactId>
    <version>1.0</version>
    <packaging>war</packaging>

    <dependencies>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.9.6</version>
        </dependency>
    </dependencies>

    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.3</version>
                    <configuration>
                        <source>1.8</source>
                        <target>1.8</target>
                        <encoding>UTF-8</encoding>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>

</project>
```

## 设计思路

SakaMichi 是基于 Servlet 实现的，利用过滤器（filter）拦截所有请求对请求进行处理，数据库访问层利用了 sql2o 库。开发者只需要实现 `Bootstrap` 接口，约定好控制器即可。

## 路由设计

一个 url 通常如下

```txt
http://example.com/
http://example.com/user/list?p=2&size=20
```

在框架中，讲 uri、action、controller 三者合一，封装成 `Route` 类

```java
/**
 * 路由
 */
public class Route {

    private final String uri;
    private final Method action;
    private final Object controller;
}
```

SakaMichi 是基于 Servlet 实现的，利用过滤器（filter）拦截所有请求对请求进行处理。filter 过来的请求有很多个，如何框架如何知道哪一个请求对应哪一个路由呢？

两个操作：

1. 解析 url，获取 uri
2. 遍历 Route 容器，找到对应的 Route

解析 url，获取 uri

```java
String uri = httpRequest.getRequestURI();
```

遍历 Route 容器，找到对应的 Route

```java
public Route matchUri(String uri) {
    for (Route route : routeList) {
        if (route.getUri().equals(uri)) {
            return route;
        }
    }
    return null;
}
```

## 控制器设计


在框架中我们把控制器放在 Route 的 controller 属性上，实际上一个请求过来之后最终是落在 controller 的某个 action 去处理的。框架是采用反射实现动态调用方法执行的

```java
Object controller = route.getController();
Method action = route.getAction();
try {
    action.invoke(controller, request, response);
} catch (IllegalAccessException e) {
    e.printStackTrace();
} catch (InvocationTargetException e) {
    e.printStackTrace();
}
```

## 视图设计

该框架可以返回页面和 json 数据

返回页面，调用的了 Servlet 的方法

```java
HttpServletRequest.getRequestDispatcher("").forward(servletRequest, servletResponse);
```

返回 json，利用 `HttpServletResponse.writer` 输出 json 内容

```java
public void json(String json) {
    httpServletResponse.setContentType("application/json; charset=utf-8");
    PrintWriter out = null;
    try {
        out = httpServletResponse.getWriter();
        out.write(json);
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (out != null) {
            out.close();
        }
    }
}
```

5. 数据库操作
