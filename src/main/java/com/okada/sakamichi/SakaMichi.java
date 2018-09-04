package com.okada.sakamichi;

import java.util.HashMap;
import java.util.Map;

public class SakaMichi {

    private static final ThreadLocal<SakaMichiContext> CONTEXT = new ThreadLocal<>();

    private final Map<String, Route> routeMap;

    private SakaMichi() {
        routeMap = new HashMap<>();
    }

    public SakaMichi addRoute(String uri, String action, Object controller) {
        Route route = new Route(uri, action, controller);
        routeMap.put(uri, route);
        return this;
    }

    public Route matchUri(String uri) {
        return routeMap.get(uri);
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
