package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.common.jpa.query.QueryHandler;
import com.xxl.job.admin.entity.User;
import com.xxl.job.admin.query.UserQuery;
import com.xxl.job.admin.repository.UserRepository;
import com.xxl.job.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author sweeter
 * @description
 * @date 2022/9/3
 */
@Transactional(rollbackFor = Exception.class,readOnly = true)
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public ResultPage<User> findPageList(UserQuery query) {
        Page<User> page = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHandler.getPredicate(root, query, criteriaBuilder), query.createPageRequest());
        return ResultPage.of(page).peek(v->v.setPassword(""));
    }

    /**
     * 修改密码
     *
     * @param password
     */
    @Transactional
    @Override
    public void changePwd(String password) {

    }

    /**
     * 删除用户
     *
     * @param id
     */
    @Transactional
    @Override
    public void delete(Long id) {
        if (1L != id) {
            userRepository.deleteById(id);
        }
    }

    /**
     * 新增用户
     *
     * @param user
     */
    @Transactional
    @Override
    public void save(User user) {
        if (Objects.nonNull(user.getId())) {
            if (StringUtils.isBlank(user.getPassword())) {
                User en = this.userRepository.getOne(user.getId());
                user.setPassword(en.getPassword());
            }else {
                String slat = DigestUtils.md5DigestAsHex(user.getUsername().getBytes());
                String passwordMd5 = DigestUtils.md5DigestAsHex((DigestUtils.md5DigestAsHex(user.getPassword().getBytes()) + slat).getBytes());
                user.setPassword(passwordMd5);
            }
        }else {
            String slat = DigestUtils.md5DigestAsHex(user.getUsername().getBytes());
            String passwordMd5 = DigestUtils.md5DigestAsHex((DigestUtils.md5DigestAsHex(user.getPassword().getBytes()) + slat).getBytes());
            user.setPassword(passwordMd5);
        }
        userRepository.save(user);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override
    public User findOne(Long id) {
        User user = new User();
        user.setId(id);
        return this.userRepository.findOne(Example.of(user)).orElseThrow(() -> new RuntimeException("记录不存在"));
    }

}
