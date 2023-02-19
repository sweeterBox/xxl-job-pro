package com.xxl.job.admin.permission;

import com.xxl.job.admin.service.AuthService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截
 *
 * @author xuxueli 2015-12-12 18:09:04
 */
@Component
public class PermissionInterceptor implements AsyncHandlerInterceptor {

	@Resource
	private AuthService authService;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true;	// proceed with the next interceptor
		}

		// if need login
		boolean needLogin = true;
		HandlerMethod method = (HandlerMethod)handler;
		Permission permission = method.getMethodAnnotation(Permission.class);
		if (permission!=null) {
			needLogin = permission.limit();
		}
		if (request.getRequestURI().contains("login.html")) {
			needLogin = false;
		}
		if (request.getRequestURI().contains("login")) {
			needLogin = false;
		}
		if (needLogin) {
			boolean flag = authService.loginCheck(request, response);
			if (!flag) {
				response.setStatus(302);
				response.setHeader("location", request.getContextPath()+"/login.html");
				return false;
			}
		}
		return true;
	}

	private boolean webHandler(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (!(handler instanceof HandlerMethod)) {
			return true;	// proceed with the next interceptor
		}

		// if need login
		boolean needLogin = true;
		HandlerMethod method = (HandlerMethod)handler;
		Permission permission = method.getMethodAnnotation(Permission.class);
		if (permission!=null) {
			needLogin = permission.limit();
		}
		if (request.getRequestURI().contains("login.html")) {
			needLogin = false;
		}
		if (request.getRequestURI().contains("login")) {
			needLogin = false;
		}

		if (needLogin) {
			boolean flag = authService.loginCheck(request, response);
			if (!flag) {
				response.setStatus(302);
				response.setHeader("location", request.getContextPath()+"/login.html");
				return false;
			}
		}
		return true;
	}

}
