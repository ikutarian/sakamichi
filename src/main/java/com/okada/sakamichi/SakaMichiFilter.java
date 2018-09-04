package com.okada.sakamichi;

import com.okada.sakamichi.config.Constants;
import com.okada.sakamichi.servlet.wrapper.Request;
import com.okada.sakamichi.servlet.wrapper.Response;
import com.okada.sakamichi.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SakaMichiFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SakaMichiFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("SakaMichi初始化");

        SakaMichi sakaMichi = SakaMichi.noboru();

        String bootstrapClassName = filterConfig.getInitParameter(Constants.InitParameter.BOOTSTRAP);
        if (StringUtils.isBlank(bootstrapClassName)) {
            throw new SakaMichiException("bootstrapClassName 不能为空");
        }
        Bootstrap bootstrap = newInstance(bootstrapClassName);
        bootstrap.init(sakaMichi);
    }

    private Bootstrap newInstance(String className) {
        try {
            Class<?> aClass = Class.forName(className);
            return (Bootstrap) aClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        throw new SakaMichiException("初始化Bootstrap失败");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpRequest.setCharacterEncoding(Constants.DEFAULT_CHAR_SET);
        httpResponse.setCharacterEncoding(Constants.DEFAULT_CHAR_SET);

        String uri = httpRequest.getRequestURI();
        SakaMichi sakaMichi = SakaMichi.noboru();
        Route route = sakaMichi.matchUri(uri);
        if (route != null) {
            log.info("匹配uri成功：" + route);
            handle(httpRequest, httpResponse, route);
        } else {
            log.warn("未找到匹配的uri: " + uri);
        }
    }

    private void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Route route) {
        Request request = new Request(httpRequest);
        Response response = new Response(httpResponse);

        SakaMichi.initContext(request, response);

        Object controller = route.getController();
        Method action = route.getAction();
        try {
            action.invoke(controller, request, response);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        log.info("destroy");
    }
}
