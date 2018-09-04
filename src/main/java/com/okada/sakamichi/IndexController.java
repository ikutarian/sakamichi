package com.okada.sakamichi;

import java.util.HashMap;
import java.util.Map;

public class IndexController {

    public void index(Request request, Response response){
        request.attr("name", "okada");
        response.view("hello");
    }

    public void json(Request request, Response response){
        Map<String, Object> json = new HashMap<>();
        json.put("code", 0);
        json.put("msg", "ok");
        json.put("data", "ojbk");
        response.json(json);
    }
}
