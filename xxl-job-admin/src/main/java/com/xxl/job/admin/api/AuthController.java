package com.xxl.job.admin.api;

import com.xxl.job.admin.permission.Permission;
import com.xxl.job.admin.model.AuthInfo;
import com.xxl.job.admin.model.ReqLogin;
import com.xxl.job.admin.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sweeter
 * @description 鉴权相关
 * @date 2022/9/4
 */
@Api(tags = "鉴权(Auth)")
@RestController
@RequestMapping("/v1.0/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @ApiOperation("登录")
    @PostMapping("login")
    @Permission(limit = false)
    public ResponseEntity<AuthInfo> login(HttpServletRequest request, HttpServletResponse response, ReqLogin req) {
        AuthInfo authVO = authService.login(request, response, req);
        return ResponseEntity.ok(authVO);
    }


    @ApiOperation("退出登录")
    @DeleteMapping("logout")
    public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok(null);
    }

}
