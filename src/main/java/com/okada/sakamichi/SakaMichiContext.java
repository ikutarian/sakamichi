package com.okada.sakamichi;

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
