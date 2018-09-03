package com.okada.sakamichi;

import javax.servlet.http.HttpServletRequest;

public class Request {

    private HttpServletRequest httpServletRequest;

    public Request(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public void attr(String name, Object value) {

    }
}
