package com.xxl.job.admin.api;

import com.xxl.job.admin.permission.Permission;
import com.xxl.job.admin.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class IndexController {

	@Resource
	private LoginService loginService;

	@RequestMapping("/login.html")
	@Permission(limit = false)
	public ModelAndView toLogin(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
		if (loginService.ifLogin(request, response) != null) {
			modelAndView.setView(new RedirectView("/", true, false));
			return modelAndView;
		}
		return new ModelAndView("login");
	}






}
