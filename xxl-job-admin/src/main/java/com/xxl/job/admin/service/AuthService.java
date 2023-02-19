package com.xxl.job.admin.service;

import com.xxl.job.admin.model.AuthInfo;
import com.xxl.job.admin.model.ReqLogin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sweeter
 * @date 2022/9/4
 */
public interface AuthService {

    /**
     * 登录系统
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param req login request param
     * @return
     */
    AuthInfo login(HttpServletRequest request, HttpServletResponse response, ReqLogin req);

    /**
     * 退出登录
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    void logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * 登录状态检查
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return if login will return true else false
     */
    boolean loginCheck(HttpServletRequest request, HttpServletResponse response);
}
