package com.okada.sakamichi.servlet.wrapper;

import javax.servlet.http.HttpServletRequest;

public class Request {

    private HttpServletRequest httpServletRequest;

    public Request(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public void attr(String name, Object value) {
        httpServletRequest.setAttribute(name, value);
    }

    public HttpServletRequest getRaw() {
        return httpServletRequest;
    }
}
