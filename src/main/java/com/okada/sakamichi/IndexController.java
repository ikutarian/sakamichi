package com.okada.sakamichi;

public class IndexController {

    public void index(Request request, Response response){
        System.out.println("IndexController: index");
    }

    public void json(Request request, Response response){
        System.out.println("IndexController: json");
    }
}
