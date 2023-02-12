package com.xxl.job.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author sweeter
 * @date 2022/11/19
 */
@RequestMapping("/")
@Controller
public class WebController {


    @GetMapping(value = {"/","index"})
    public String index() {
        return "index";
    }

    @GetMapping("/login.html")
    public String loginHtml() {
        return "login";
    }

    @GetMapping("/application")
    public String application() {
        return "application/index";
    }

    @GetMapping("/application/clientList")
    public String clientList() {
        return "application/clientList";
    }

    @GetMapping("/jobcode")
    public String jobcode() {
        return "jobcode/jobcode.index";
    }

    @GetMapping("/joblog/logDetailPage")
    public String logDetailPage() {
        return "joblog/joblog.detail";
    }

    @GetMapping("/user")
    public String user() {
        return "user/index";
    }

    @GetMapping("/log")
    public String log() {
        return "log/index";
    }

    @GetMapping("/task")
    public String task() {
        return "task/index";
    }


}
