package com.xxl.job.admin.service;

import com.xxl.job.admin.entity.User;
import com.xxl.job.admin.core.util.CookieUtil;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.repository.UserRepository;
import com.xxl.job.model.R;
import com.xxl.job.utils.JsonUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * @author xuxueli 2019-05-04 22:13:264
 */
@Configuration
public class LoginService {

    public static final String LOGIN_IDENTITY_KEY = "XXL_JOB_LOGIN_IDENTITY";

    @Resource
    private UserRepository userRepository;


    private String makeToken(User user){
        String tokenJson = JsonUtils.obj2Json(user);
        String tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
        return tokenHex;
    }
    private User parseToken(String tokenHex){
        User user = null;
        if (tokenHex != null) {
            String tokenJson = new String(new BigInteger(tokenHex, 16).toByteArray());
            user = JsonUtils.json2Obj(tokenJson, User.class);
        }
        return user;
    }


    public R<String> login(HttpServletRequest request, HttpServletResponse response, String username, String password, boolean ifRemember){

        // param
        if (username==null || username.trim().length()==0 || password==null || password.trim().length()==0){
            return new R<>(500, I18nUtil.getString("login_param_empty"));
        }

        // valid passowrd
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new R<>(500, I18nUtil.getString("login_param_unvalid"));
        }
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!passwordMd5.equals(user.getPassword())) {
            return new R<>(500, I18nUtil.getString("login_param_unvalid"));
        }

        String loginToken = makeToken(user);

        // do login
        CookieUtil.set(response, LOGIN_IDENTITY_KEY, loginToken, ifRemember);
        return R.SUCCESS;
    }

    /**
     * logout
     *
     * @param request
     * @param response
     */
    public R<String> logout(HttpServletRequest request, HttpServletResponse response){
        CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
        return R.SUCCESS;
    }

    /**
     * logout
     *
     * @param request
     * @return
     */
    public User ifLogin(HttpServletRequest request, HttpServletResponse response){
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
                        return dbUser;
                    }
                }
            }
        }
        return null;
    }


}
