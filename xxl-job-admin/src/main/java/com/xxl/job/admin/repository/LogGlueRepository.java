package com.xxl.job.admin.repository;

import com.xxl.job.admin.common.jpa.repository.BaseJpaRepository;
import com.xxl.job.admin.entity.LogGlue;
import java.util.List;
/**
 * @author sweeter
 * @date 2022/9/3
 */
public interface LogGlueRepository extends BaseJpaRepository<LogGlue, Long> {

    void deleteByTaskId(Long taskId);

    List<LogGlue> findAllByTaskId(Long taskId);


}
