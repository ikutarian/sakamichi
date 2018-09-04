package com.okada.sakamichi;

import com.okada.sakamichi.servlet.wrapper.Request;
import com.okada.sakamichi.servlet.wrapper.Response;

public class SakaMichiContext {

    private Request request;
    private Response response;

    public SakaMichiContext(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}
