package com.xxl.job.admin.repository;

import com.xxl.job.admin.common.jpa.repository.BaseJpaRepository;
import com.xxl.job.admin.entity.Application;
import java.util.List;

/**
 * @author sweeter
 * @date 2022/9/3
 */
public interface ApplicationRepository extends BaseJpaRepository<Application, Long> {

    boolean existsByAndName(String name);

    List<Application> findAllByNameIn(List<String> names);


}
