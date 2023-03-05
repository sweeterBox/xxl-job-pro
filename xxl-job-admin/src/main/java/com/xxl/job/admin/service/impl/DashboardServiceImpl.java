package com.xxl.job.admin.service.impl;

import com.sun.management.OperatingSystemMXBean;
import com.xxl.job.admin.entity.LogReport;
import com.xxl.job.admin.enums.InstanceStatus;
import com.xxl.job.admin.enums.TriggerStatus;
import com.xxl.job.admin.model.StatisticsInfo;
import com.xxl.job.admin.model.SystemInfo;
import com.xxl.job.admin.repository.*;
import com.xxl.job.admin.service.DashboardService;
import com.xxl.job.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

        if (logReportList != null && logReportList.size() > 0) {
            for (LogReport item : logReportList) {
                String day = DateUtil.formatDate(item.getTriggerDay());
                Integer triggerDayCountRunning = Optional.ofNullable(item.getRunningCount()).orElse(0);
                Integer triggerDayCountSuc = Optional.ofNullable(item.getSucCount()).orElse(0);
                Integer triggerDayCountFail = Optional.ofNullable(item.getFailCount()).orElse(0);

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

        Map<String, Object> result = new HashMap<>();
        result.put("triggerDayList", triggerDayList);
        result.put("triggerDayCountRunningList", triggerDayCountRunningList);
        result.put("triggerDayCountSucList", triggerDayCountSucList);
        result.put("triggerDayCountFailList", triggerDayCountFailList);

        result.put("triggerCountRunningTotal", triggerCountRunningTotal);
        result.put("triggerCountSucTotal", triggerCountSucTotal);
        result.put("triggerCountFailTotal", triggerCountFailTotal);
        return result;


    }

    /**
     * //TODO 优化为查询报表
     * @return
     */
    @Override
    public StatisticsInfo statistics() {
        StatisticsInfo info = new StatisticsInfo();
        Long taskAllNum = taskRepository.count();
        Long taskRunningNum = taskRepository.countAllByTriggerStatus(TriggerStatus.ENABLE);
        info.setTaskAllNum(taskAllNum);
        info.setTaskRunningNum(taskRunningNum);

        Long instanceAllNum = instanceRepository.count();
        Long instanceUpNum = instanceRepository.countAllByStatus(InstanceStatus.UP);
        info.setInstanceAllNum(instanceAllNum);
        info.setInstanceUpNum(instanceUpNum);

        Long triggerAllNum = logRepository.count();
        Long triggerSuccessNum = logRepository.countAllByTriggerStatusAndHandleStatus(200, 200);
        info.setTriggerAllNum(triggerAllNum);
        info.setTriggerSuccessNum(triggerSuccessNum);
        return info;
    }

    @Override
    public SystemInfo findSystemInfo() {
        SystemInfo info = new SystemInfo();
        try {

            OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            MemoryUsage nonMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

            info.setHeapMemory(new SystemInfo.MemoryUsage(heapMemoryUsage.getInit(), heapMemoryUsage.getMax(), heapMemoryUsage.getUsed()));
            info.setNonHeapMemory(new SystemInfo.MemoryUsage(nonMemoryUsage.getInit(), nonMemoryUsage.getMax(), nonMemoryUsage.getUsed()));

            String osName = System.getProperty("os.name");
            info.setOsName(osName);

            //系统物理内存
            info.setPhysicalMemory(new SystemInfo.PhysicalMemory(osmxb.getTotalPhysicalMemorySize(), osmxb.getFreePhysicalMemorySize(), osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()));

            ThreadGroup parentThread;
            for (parentThread = Thread.currentThread().getThreadGroup(); parentThread.getParent() != null; parentThread = parentThread.getParent()) {

            }

            int totalThread = parentThread.activeCount();
            info.setTotalThread(totalThread);

            List<SystemInfo.Space> spaces = new ArrayList<>();
            File[] files = File.listRoots();
            for (File file : files) {
                SystemInfo.Space space = new SystemInfo.Space();
                space.setFreeSpace(file.getFreeSpace());
                space.setName(file.getPath());
                space.setTotalSpace(file.getTotalSpace());
                space.setUsableSpace(file.getUsableSpace());
                spaces.add(space);
            }
            info.setSpaces(spaces);

            info.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(ManagementFactory.getRuntimeMXBean().getStartTime())));
            info.setPid(System.getProperty("PID"));
            info.setCpuCoreSize(Runtime.getRuntime().availableProcessors());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }
}
