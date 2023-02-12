package com.xxl.job.admin.service;

import com.xxl.job.admin.entity.LogReport;

import java.util.Date;

/**
 * @author sweeter
 * @date 2022/9/3
 */
public interface LogReportService {

    void save(LogReport en);

    LogReport findByTriggerDay(Date triggerDay);


}
