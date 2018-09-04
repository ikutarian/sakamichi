package com.okada.sakamichi;

import com.okada.sakamichi.servlet.wrapper.Request;
import com.okada.sakamichi.servlet.wrapper.Response;

import java.util.ArrayList;
import java.util.List;

public class SakaMichi {

    private static final ThreadLocal<SakaMichiContext> CONTEXT = new ThreadLocal<>();

    // 采用优先匹配
    private final List<Route> routeList;

    private SakaMichi() {
        routeList = new ArrayList<>();
    }

    public SakaMichi addRoute(String uri, String action, Object controller) {
        Route route = new Route(uri, action, controller);
        routeList.add(route);
        return this;
    }

    public Route matchUri(String uri) {
        for (Route route : routeList) {
            if (route.getUri().equals(uri)) {
                return route;
            }
        }
        return null;
    }

    public static void initContext(Request request, Response response) {
        SakaMichiContext marioContext = new SakaMichiContext(request, response);
        CONTEXT.set(marioContext);
    }

    public static SakaMichiContext getContext() {
        return CONTEXT.get();
    }

    private static class InstanceHolder {
        private static SakaMichi instance = new SakaMichi();
    }

    public static SakaMichi noboru(){
        return InstanceHolder.instance;
    }
}
