package com.xxl.job.admin.service;

import com.xxl.job.admin.model.AuthInfo;
import com.xxl.job.admin.model.ReqLogin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sweeter
 * @description
 * @date 2022/9/4
 */
public interface AuthService {

    AuthInfo login(HttpServletRequest request, HttpServletResponse response, ReqLogin req);
}
