package com.xxl.job.admin.core.thread;

import com.xxl.job.admin.repository.LogRepository;
import com.xxl.job.utils.SpringContextUtils;
import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.entity.LogReport;
import com.xxl.job.admin.service.LogReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * job log report helper
 *
 * @author xuxueli 2019-11-22
 */
public class JobLogReportHelper {
    private static Logger logger = LoggerFactory.getLogger(JobLogReportHelper.class);

    private static JobLogReportHelper instance = new JobLogReportHelper();
    public static JobLogReportHelper getInstance(){
        return instance;
    }


    private Thread logrThread;
    private volatile boolean toStop = false;
    public void start(){
        logrThread = new Thread(() -> {
            // last clean log time
            long lastCleanLogTime = 0;
            while (!toStop) {
                // 1、log-report refresh: refresh log report in 3 days
                try {
                    for (int i = 0; i < 3; i++) {
                        // today
                        Calendar itemDay = Calendar.getInstance();
                        itemDay.add(Calendar.DAY_OF_MONTH, -i);
                        itemDay.set(Calendar.HOUR_OF_DAY, 0);
                        itemDay.set(Calendar.MINUTE, 0);
                        itemDay.set(Calendar.SECOND, 0);
                        itemDay.set(Calendar.MILLISECOND, 0);

                        Date todayFrom = itemDay.getTime();

                        itemDay.set(Calendar.HOUR_OF_DAY, 23);
                        itemDay.set(Calendar.MINUTE, 59);
                        itemDay.set(Calendar.SECOND, 59);
                        itemDay.set(Calendar.MILLISECOND, 999);

                        Date todayTo = itemDay.getTime();

                        // refresh log-report every minute
                        LogReport logReport = new LogReport();
                        logReport.setTriggerDay(todayFrom);

                        Map<String, Long> triggerCountMap = new HashMap<>();// SpringContextUtils.getBean(LogRepository.class).findLogReport(todayFrom, todayTo);
                        if (triggerCountMap!=null && triggerCountMap.size()>0) {
                            int triggerDayCount = triggerCountMap.containsKey("triggerDayCount")?Integer.valueOf(String.valueOf(triggerCountMap.get("triggerDayCount"))):0;
                            int triggerDayCountRunning = triggerCountMap.containsKey("triggerDayCountRunning")?Integer.valueOf(String.valueOf(triggerCountMap.get("triggerDayCountRunning"))):0;
                            int triggerDayCountSuc = triggerCountMap.containsKey("triggerDayCountSuc")?Integer.valueOf(String.valueOf(triggerCountMap.get("triggerDayCountSuc"))):0;
                            int triggerDayCountFail = triggerDayCount - triggerDayCountRunning - triggerDayCountSuc;

                            logReport.setRunningCount(triggerDayCountRunning);
                            logReport.setSucCount(triggerDayCountSuc);
                            logReport.setFailCount(triggerDayCountFail);
                        }
                        //先查询是否存在

                        // do refresh
                        LogReportService logReportService = SpringContextUtils.getBean(LogReportService.class);
                        LogReport report = logReportService.findByTriggerDay(todayFrom);
                        if (Objects.nonNull(report)) {
                            logReport.setId(report.getId());
                        }else {
                            logReport.setRunningCount(0);
                            logReport.setSucCount(0);
                            logReport.setFailCount(0);
                        }
                        logReportService.save(logReport);
                    }

                } catch (Exception e) {
                    if (!toStop) {
                        logger.error(" xxl-job, job log report thread error:{}", e);
                    }
                }

                // 2、log-clean: switch open & once each day
                //清理过期的汇总记录
                if (System.currentTimeMillis() - lastCleanLogTime > 24*60*60*1000) {


                }

                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (Exception e) {
                    if (!toStop) {
                        logger.error(e.getMessage(), e);
                    }
                }

            }

            logger.info(" xxl-job, job log report thread stop");

        });
        logrThread.setDaemon(true);
        logrThread.setName("xxl-job, admin JobLogReportHelper");
        logrThread.start();
    }

    public void toStop(){
        toStop = true;
        // interrupt and wait
        logrThread.interrupt();
        try {
            logrThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
