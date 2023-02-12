package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.entity.LogReport;
import com.xxl.job.admin.repository.*;
import com.xxl.job.admin.service.DashboardService;
import com.xxl.job.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author sweeter
 * @date 2022/12/24
 */
@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private ApplicationRepository applicationRepository;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    public LogRepository logRepository;

    @Resource
    private InstanceRepository instanceRepository;

    @Resource
    private LogReportRepository logReportRepository;

    @Override
    public Map<String, Object> chart(Date startDate, Date endDate) {

        List<String> triggerDayList = new ArrayList<String>();
        List<Integer> triggerDayCountRunningList = new ArrayList<Integer>();
        List<Integer> triggerDayCountSucList = new ArrayList<Integer>();
        List<Integer> triggerDayCountFailList = new ArrayList<Integer>();
        int triggerCountRunningTotal = 0;
        int triggerCountSucTotal = 0;
        int triggerCountFailTotal = 0;

        List<LogReport> logReportList = logReportRepository.findAllByTriggerDayIsBetweenOrderByTriggerDayAsc(startDate, endDate);

        if (logReportList!=null && logReportList.size()>0) {
            for (LogReport item: logReportList) {
                String day = DateUtil.formatDate(item.getTriggerDay());
                int triggerDayCountRunning = item.getRunningCount();
                int triggerDayCountSuc = item.getSucCount();
                int triggerDayCountFail = item.getFailCount();

                triggerDayList.add(day);
                triggerDayCountRunningList.add(triggerDayCountRunning);
                triggerDayCountSucList.add(triggerDayCountSuc);
                triggerDayCountFailList.add(triggerDayCountFail);

                triggerCountRunningTotal += triggerDayCountRunning;
                triggerCountSucTotal += triggerDayCountSuc;
                triggerCountFailTotal += triggerDayCountFail;
            }
        } else {
            for (int i = -6; i <= 0; i++) {
                triggerDayList.add(DateUtil.formatDate(DateUtil.addDays(new Date(), i)));
                triggerDayCountRunningList.add(0);
                triggerDayCountSucList.add(0);
                triggerDayCountFailList.add(0);
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("triggerDayList", triggerDayList);
        result.put("triggerDayCountRunningList", triggerDayCountRunningList);
        result.put("triggerDayCountSucList", triggerDayCountSucList);
        result.put("triggerDayCountFailList", triggerDayCountFailList);

        result.put("triggerCountRunningTotal", triggerCountRunningTotal);
        result.put("triggerCountSucTotal", triggerCountSucTotal);
        result.put("triggerCountFailTotal", triggerCountFailTotal);
        return result;


    }

    @Override
    public Map<String, Object> statistics() {
        Long jobInfoCount = taskRepository.count();
        Long executorCount = instanceRepository.count();

        Map<String, Object> dashboardMap = new HashMap<>();
        dashboardMap.put("jobInfoCount", jobInfoCount);
        dashboardMap.put("jobLogCount", 0);
        dashboardMap.put("jobLogSuccessCount", 0);
        dashboardMap.put("executorCount", executorCount);
        return dashboardMap;
    }
}
