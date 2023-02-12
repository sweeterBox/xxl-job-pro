package com.xxl.job.admin.repository;

import com.xxl.job.admin.common.jpa.repository.BaseJpaRepository;
import com.xxl.job.admin.entity.LogReport;
import java.util.Date;
import java.util.List;
/**
 * @author sweeter
 * @date 2022/9/3
 */
public interface LogReportRepository extends BaseJpaRepository<LogReport, Long> {

    List<LogReport> findAllByTriggerDayIsBetweenOrderByTriggerDayAsc(Date triggerDayFrom, Date triggerDayTo);

    LogReport findByTriggerDay(Date triggerDay);



}
