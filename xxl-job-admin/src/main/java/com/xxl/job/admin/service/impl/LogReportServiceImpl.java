package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.entity.LogReport;
import com.xxl.job.admin.repository.LogReportRepository;
import com.xxl.job.admin.service.LogReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author sweeter
 * @description
 * @date 2022/9/3
 */
@Transactional(rollbackFor = Exception.class,readOnly = true)
@Slf4j
@Service
public class LogReportServiceImpl implements LogReportService {

    @Resource
    private LogReportRepository logReportRepository;

    @Transactional(readOnly = false)
    @Override
    public void save(LogReport en) {
        logReportRepository.save(en);
    }

    @Override
    public LogReport findByTriggerDay(Date triggerDay) {
        return logReportRepository.findByTriggerDay(triggerDay);
    }


}
