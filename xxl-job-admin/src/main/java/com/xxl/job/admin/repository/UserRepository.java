package com.xxl.job.admin.repository;

import com.xxl.job.admin.common.jpa.repository.BaseJpaRepository;
import com.xxl.job.admin.entity.User;

/**
 * @author sweeter
 * @description
 * @date 2022/9/3
 */
public interface UserRepository extends BaseJpaRepository<User, Long> {

    User findByUsername(String username);

}
