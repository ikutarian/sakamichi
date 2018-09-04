package com.okada.sakamichi;

import java.lang.reflect.Method;

/**
 * 路由
 */
public class Route {

    private final String uri;
    private final Method action;
    private final Object controller;

    public Route(String uri, String action, Object controller) {
        Validate.notBlank(uri, "uri不能为空");
        Validate.notBlank(action, "action不能为空");
        Validate.notNull(controller, "controller不能为空");

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
