package com.okada.sakamichi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App implements Bootstrap {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    @Override
    public void init(SakaMichi sakaMichi) {
        log.info("添加路由");

        IndexController indexController = new IndexController();
        sakaMichi.addRoute("/", "index", indexController);
        sakaMichi.addRoute("/json", "json", indexController);

        UserController userController = new UserController();
        sakaMichi.addRoute("/user/list", "list", userController);
        sakaMichi.addRoute("/user/create", "create", userController);
        sakaMichi.addRoute("/user/delete", "delete", userController);
        sakaMichi.addRoute("/user/update", "update", userController);

        log.info("添加路由 完毕");
    }
}
