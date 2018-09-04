package com.okada.sakamichi;

import com.okada.sakamichi.servlet.wrapper.Request;
import com.okada.sakamichi.servlet.wrapper.Response;
import com.okada.sakamichi.util.ValidateUtils;

import java.lang.reflect.Method;

/**
 * 路由
 */
public class Route {

    private final String uri;
    private final Method action;
    private final Object controller;

    public Route(String uri, String action, Object controller) {
        ValidateUtils.notBlank(uri, "uri不能为空");
        ValidateUtils.notBlank(action, "action不能为空");
        ValidateUtils.notNull(controller, "controller不能为空");

        this.uri = uri;
        try {
            this.action = controller.getClass().getMethod(action, Request.class, Response.class);
        } catch (NoSuchMethodException e) {
            throw new SakaMichiException(controller.getClass().getName()
                    + "中不存在" + action + "(Request, Response)方法");
        }
        this.controller = controller;
    }

    public String getUri() {
        return uri;
    }

    public Method getAction() {
        return action;
    }

    public Object getController() {
        return controller;
    }

    @Override
    public String toString() {
        return "Route{" +
                "uri='" + uri + '\'' +
                ", action=" + action.getName() +
                ", controller=" + controller.getClass().getName() +
                '}';
    }
}
