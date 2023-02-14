package com.xxl.job.admin.repository;

import com.xxl.job.admin.common.jpa.repository.BaseJpaRepository;
import com.xxl.job.admin.entity.Log;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author sweeter
 * @date 2022/9/3
 */
public interface LogRepository extends BaseJpaRepository<Log, Long> {

	void deleteByTaskId(Long taskId);


	@Deprecated
	@Modifying
	@Query(value = "UPDATE xxl_job_log SET alarm_status = ?3  WHERE id= ?1  AND alarm_status = ?2", nativeQuery = true)
	int updateAlarmStatus(Long logId, Integer oldAlarmStatus, Integer newAlarmStatus);


	@Deprecated
	@Query(value = "SELECT  t.id FROM xxl_job_log t LEFT JOIN xxl_job_instance t2 ON t.instance_url = t2.url  WHERE t.trigger_status = 200 AND t.handle_status = 0 AND t.trigger_time  <= ?1 AND t2.id IS NULL", nativeQuery = true)
	List<Long> findLostJobIds(Date losedTime);


	@Deprecated
	@Query(value = "SELECT IFNULL(COUNT(handle_status),0) triggerDayCount ,IFNULL(SUM(CASE WHEN (trigger_status in (0, 200) and handle_status = 0) then 1 else 0 end),0) as triggerDayCountRunning,IFNULL(SUM(CASE WHEN handle_status = 200 then 1 else 0 end),0) as triggerDayCountSuc  FROM xxl_job_log WHERE trigger_time BETWEEN ?1  and ?2", nativeQuery = true)
	Map<String, Long> findLogReport(Date from, Date to);

}
