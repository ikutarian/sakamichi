package com.okada.sakamichi;

import java.util.HashMap;
import java.util.Map;

public class SakaMichi {

    private final Map<String, Route> routeMap;

    public SakaMichi() {
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

    private static class InstanceHolder {
        private static SakaMichi instance = new SakaMichi();
    }

    public static SakaMichi noboru(){
        return InstanceHolder.instance;
    }
}
