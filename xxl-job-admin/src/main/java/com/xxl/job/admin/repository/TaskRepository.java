package com.xxl.job.admin.repository;

import com.xxl.job.admin.common.jpa.repository.BaseJpaRepository;
import com.xxl.job.admin.entity.Task;
import com.xxl.job.admin.enums.TriggerStatus;


/**
 * @author sweeter
 * @date 2022/9/3
 */
public interface TaskRepository extends BaseJpaRepository<Task, Long> {

    Long countAllByTriggerStatus(TriggerStatus triggerStatus);


    boolean existsAllByExecutorHandler(String executorHandler);

    Task findByExecutorHandler(String executorHandler);


}
