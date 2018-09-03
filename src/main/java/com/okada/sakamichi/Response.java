package com.okada.sakamichi;

import javax.servlet.http.HttpServletResponse;

public class Response {

    private HttpServletResponse httpServletResponse;

    public Response(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public void json(String json) {

    }

    public void view(String templateName) {
    }
}
