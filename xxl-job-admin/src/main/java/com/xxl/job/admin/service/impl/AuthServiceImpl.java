package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.core.util.CookieUtil;
import com.xxl.job.admin.entity.User;
import com.xxl.job.admin.model.AuthInfo;
import com.xxl.job.admin.model.ReqLogin;
import com.xxl.job.admin.repository.UserRepository;
import com.xxl.job.admin.service.AuthService;
import com.xxl.job.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * @author sweeter
 * @date 2022/9/4
 */
@Transactional(rollbackFor = Exception.class,readOnly = true)
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    public static final String LOGIN_IDENTITY_KEY = "XXL_JOB_LOGIN_IDENTITY";

    @Resource
    private UserRepository userRepository;

    @Override
    public AuthInfo login(HttpServletRequest request, HttpServletResponse response, ReqLogin req) {
        User user = userRepository.findByUsername(req.getUserName());
        if (user == null) {
            return new AuthInfo(null, false,null);
        }
        String slat = DigestUtils.md5DigestAsHex(user.getUsername().getBytes());
        String passwordMd5 = DigestUtils.md5DigestAsHex((DigestUtils.md5DigestAsHex(req.getPassword().getBytes()) + slat).getBytes());
        if (!passwordMd5.equals(user.getPassword())) {
            return new AuthInfo(null, false,null);
        }
        String loginToken = makeToken(user);
        CookieUtil.set(response, LOGIN_IDENTITY_KEY, loginToken, true);
        return new AuthInfo(loginToken, true,new AuthInfo.UserInfo(user.getUsername()));
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);

    }

    /**
     * 登录状态检查
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return if login will return true else false
     */
    @Override
    public boolean loginCheck(HttpServletRequest request, HttpServletResponse response) {
        String cookieToken = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
        if (cookieToken != null) {
            User cookieUser = null;
            try {
                cookieUser = parseToken(cookieToken);
            } catch (Exception e) {
                logout(request, response);
            }
            if (cookieUser != null) {
                User dbUser = userRepository.findByUsername(cookieUser.getUsername());
                if (dbUser != null) {
                    if (cookieUser.getPassword().equals(dbUser.getPassword())) {
                        return true;
                    }
                }
                request.setAttribute(LOGIN_IDENTITY_KEY, cookieUser);
            }
        }
        return false;
    }

    private String makeToken(User user) {
        String tokenJson = JsonUtils.obj2Json(user);
        String tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
        return tokenHex;
    }

    private User parseToken(String tokenHex) {
        User user = null;
        if (tokenHex != null) {
            String tokenJson = new String(new BigInteger(tokenHex, 16).toByteArray());
            user = JsonUtils.json2Obj(tokenJson, User.class);
        }
        return user;

    }
}
