package com.xxl.job.admin.repository;

import com.xxl.job.admin.common.jpa.repository.BaseJpaRepository;
import com.xxl.job.admin.entity.Task;
import com.xxl.job.admin.enums.TriggerStatus;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


/**
 * @author sweeter
 * @date 2022/9/3
 */
public interface TaskRepository extends BaseJpaRepository<Task, Long> {

    @Query(value = "SELECT t.* FROM xxl_job_task AS t WHERE t.trigger_status = 1 and t.next_trigger_time  <=  ?1 ORDER BY id ASC LIMIT ?2", nativeQuery = true)
    List<Task> scheduleJobQuery(Long maxNextTime, Long limitSize);

    Long countAllByTriggerStatus(TriggerStatus triggerStatus);


}
