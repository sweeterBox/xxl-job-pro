package com.xxl.job.admin.service;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.entity.User;
import com.xxl.job.admin.query.UserQuery;

/**
 * @author sweeter
 * @description
 * @date 2022/9/3
 */
public interface UserService {

    ResultPage<User> findPageList(UserQuery query);

    /**
     * 修改密码
     * @param password
     */
    void changePwd(String password);

    /**
     * 删除用户
     * @param id
     */
    void delete(Long id);

    /**
     * 新增用户
     * @param user
     */
    void save(User user);

    /**
     * 查询详情
     * @param id
     * @return
     */
    User findOne(Long id);
}
