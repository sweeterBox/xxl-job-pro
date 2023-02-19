package com.xxl.job.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sweeter
 * @date 2022/11/26
 */
@Controller
public class UiController {

    @Autowired
    private ApplicationContext applicationContext;



    @RequestMapping("menus")
    public ResponseEntity<List<Menu>> menus() {
        String contextPath = applicationContext.getEnvironment().getProperty("server.servlet.context-path");
        List<Menu> menus = new ArrayList<>();
        menus.add(new Menu("nav-icon fas fa-table", "application", "应用管理", contextPath + "/application", "_self", 1));
        menus.add(new Menu("nav-icon fas fa-th", "user", "用户管理", contextPath +"/user", "_self",4));
        menus.add(new Menu("nav-icon fas fa-book", "log", "调度日志", contextPath +"/log", "_self",3));
        menus.add(new Menu("nav-icon fas fa-sync-alt", "task", "调度任务", contextPath +"/task", "_self",2));
       // menus.add(new Menu("nav-icon fas fa-tachometer-alt", "index", "Dashboard", contextPath +"/", "_self",0));
        menus.add(new Menu("nav-icon fas fa-tachometer-alt", "dashboard", "信息看板", contextPath +"/index", "_self", 0));
        menus.add(new Menu("nav-icon fas fa-file", "api-doc", "Api Doc", contextPath +"/doc.html", "_blank", 5));
       return ResponseEntity.ok(menus.stream().sorted(Comparator.comparingInt(Menu::getOrder)).collect(Collectors.toList()));
    }



}
