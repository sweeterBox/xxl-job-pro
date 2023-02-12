package com.xxl.job.admin.model;

import lombok.Data;

import java.util.List;

/**
 * @author sweeter
 * @description
 * @date 2022/9/10
 */
@Data
public class UserInfo {

    private List<String> roles;

    private String avatar = "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif";

    private String name = "ADMIN";

    private String introduction;

    /**
     *
     *     roles: ['admin'],
     *     introduction: 'I am a super administrator',
     *     avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
     *     name: 'Super Admin'
     */

}
