package com.xxl.job.admin.repository;

import com.xxl.job.admin.common.jpa.repository.BaseJpaRepository;
import com.xxl.job.admin.entity.Log;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.Map;

/**
 * @author sweeter
 * @date 2022/9/3
 */
public interface LogRepository extends BaseJpaRepository<Log, Long> {

	void deleteByTaskId(Long taskId);


	@Deprecated
	@Query(value = "SELECT COUNT(handle_status) triggerDayCount ,SUM(CASE WHEN (trigger_status in (0, 200) and handle_status = 0) then 1 else 0 end) as triggerDayCountRunning,SUM(CASE WHEN handle_status = 200 then 1 else 0 end) as triggerDayCountSuc  FROM xxl_job_log WHERE trigger_time BETWEEN ?1  and ?2", nativeQuery = true)
	Map<String, Long> findLogReport(Date from, Date to);

	Long countAllByTriggerStatusAndHandleStatus(Integer triggerStatus, Integer handleStatus);
}
