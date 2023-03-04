package com.xxl.job.admin.core.thread;

import com.xxl.job.admin.repository.TaskRepository;
import com.xxl.job.admin.service.TaskService;
import com.xxl.job.utils.SpringContextUtils;
import com.xxl.job.admin.config.SystemProperties;
import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.core.cron.CronExpression;
import com.xxl.job.admin.entity.Task;
import com.xxl.job.admin.core.scheduler.MisfireStrategyEnum;
import com.xxl.job.admin.core.scheduler.ScheduleTypeEnum;
import com.xxl.job.admin.core.trigger.TriggerTypeEnum;
import com.xxl.job.admin.enums.TriggerStatus;
import com.xxl.job.admin.lock.DbLock;
import com.xxl.job.admin.lock.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author xuxueli 2019-05-21
 */
public class JobScheduleHelper {
    private static Logger logger = LoggerFactory.getLogger(JobScheduleHelper.class);

    private static JobScheduleHelper instance = new JobScheduleHelper();

    public static JobScheduleHelper getInstance(){
        return instance;
    }

    // 预读取时间 单位/毫秒
    public static final long PRE_READ_MS = 5000;

    private Thread scheduleThread;

    private Thread ringThread;

    private volatile boolean scheduleThreadToStop = false;

    private volatile boolean ringThreadToStop = false;

    private volatile static Map<Integer, List<Integer>> ringData = new ConcurrentHashMap<>();

    public void start(){

        // schedule thread
        scheduleThread = new Thread(() -> {

            try {
                TimeUnit.MILLISECONDS.sleep(5000 - System.currentTimeMillis()%1000 );
            } catch (InterruptedException e) {
                if (!scheduleThreadToStop) {
                    logger.error(e.getMessage(), e);
                }
            }
            logger.info("init xxl-job admin scheduler success.");

            // pre-read count: treadpool-size * trigger-qps (each trigger cost 50ms, qps = 1000/50 = 20)
            SystemProperties systemProperties = SpringContextUtils.getBean(SystemProperties.class);
            long preReadCount = Math.max((systemProperties.getTriggerPoolFastMax() + systemProperties.getTriggerPoolSlowMax()) * 20,5);

            while (!scheduleThreadToStop) {

                // Scan Job
                long start = System.currentTimeMillis();

                boolean preReadSuc = true;
                Lock lock = SpringContextUtils.getBean(DbLock.class);
                String lockKey = lock.createKeyByParams("schedule");
                try {
                    if (lock.lock(lockKey)) {
                        // tx start

                        // 1、pre read
                        long nowTime = System.currentTimeMillis();
                        //读取即将执行的任务
                        List<Task> scheduleList = SpringContextUtils.getBean(TaskService.class).findScheduleTasks(nowTime + PRE_READ_MS, preReadCount);
                        if (scheduleList!=null && scheduleList.size()>0) {
                            // 2、push time-ring
                            for (Task jobInfo: scheduleList) {

                                // time-ring jump
                                if (nowTime > jobInfo.getNextTriggerTime() + PRE_READ_MS) {
                                    // 2.1、trigger-expire > 5s：pass && make next-trigger-time
                                    logger.warn(" xxl-job, schedule misfire, jobId = " + jobInfo.getId());

                                    // 1、misfire match
                                    MisfireStrategyEnum misfireStrategyEnum = MisfireStrategyEnum.match(jobInfo.getMisfireStrategy().name(), MisfireStrategyEnum.DO_NOTHING);
                                    if (MisfireStrategyEnum.FIRE_ONCE_NOW == misfireStrategyEnum) {
                                        // FIRE_ONCE_NOW 》 trigger
                                        TaskTriggerThread.trigger(jobInfo.getId(), TriggerTypeEnum.MISFIRE, -1, null, null);
                                        logger.debug(" xxl-job, schedule push trigger : jobId = " + jobInfo.getId() );
                                    }

                                    // 2、fresh next
                                    refreshNextValidTime(jobInfo, new Date());

                                } else if (nowTime > jobInfo.getNextTriggerTime()) {
                                    // 2.2、trigger-expire < 5s：direct-trigger && make next-trigger-time

                                    // 1、trigger
                                    TaskTriggerThread.trigger(jobInfo.getId(), TriggerTypeEnum.CRON, -1, null, null);
                                    logger.debug(" xxl-job, schedule push trigger : jobId = " + jobInfo.getId() );

                                    // 2、fresh next
                                    refreshNextValidTime(jobInfo, new Date());

                                    // next-trigger-time in 5s, pre-read again
                                    if (TriggerStatus.ENABLE.equals(jobInfo.getTriggerStatus()) && nowTime + PRE_READ_MS > jobInfo.getNextTriggerTime()) {

                                        // 1、make ring second
                                        int ringSecond = (int)((jobInfo.getNextTriggerTime()/1000)%60);

                                        // 2、push time ring
                                        pushTimeRing(ringSecond, jobInfo.getId().intValue());

                                        // 3、fresh next
                                        refreshNextValidTime(jobInfo, new Date(jobInfo.getNextTriggerTime()));

                                    }

                                } else {
                                    // 2.3、trigger-pre-read：time-ring trigger && make next-trigger-time

                                    // 1、make ring second
                                    int ringSecond = (int)((jobInfo.getNextTriggerTime()/1000)%60);

                                    // 2、push time ring
                                    pushTimeRing(ringSecond, jobInfo.getId().intValue());

                                    // 3、fresh next
                                    refreshNextValidTime(jobInfo, new Date(jobInfo.getNextTriggerTime()));

                                }

                            }

                            // 3、update trigger info
                            for (Task jobInfo: scheduleList) {
                                SpringContextUtils.getBean(TaskRepository.class).save(jobInfo);
                            }

                        } else {
                            preReadSuc = false;
                        }
                        lock.unlock(lockKey);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!scheduleThreadToStop) {
                        logger.error("xxl-job, JobScheduleHelper#scheduleThread error:{}", e.getMessage());
                    }
                }

                long cost = System.currentTimeMillis() - start;
                // Wait seconds, align second
                if (cost < 1000) {  // scan-overtime, not wait
                    try {
                        // pre-read period: success > scan each second; fail > skip this period;
                        TimeUnit.MILLISECONDS.sleep((preReadSuc?1000:PRE_READ_MS) - System.currentTimeMillis()%1000);
                    } catch (InterruptedException e) {
                        if (!scheduleThreadToStop) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }

            }

            logger.info(" xxl-job, JobScheduleHelper#scheduleThread stop");
        });
        scheduleThread.setDaemon(true);
        scheduleThread.setName("xxl-job, admin JobScheduleHelper#scheduleThread");
        scheduleThread.start();


        // ring thread
        ringThread = new Thread(() -> {

            while (!ringThreadToStop) {

                // align second
                try {
                    TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
                } catch (InterruptedException e) {
                    if (!ringThreadToStop) {
                        logger.error(e.getMessage(), e);
                    }
                }

                try {
                    // second data
                    List<Integer> ringItemData = new ArrayList<>();
                    int nowSecond = Calendar.getInstance().get(Calendar.SECOND);   // 避免处理耗时太长，跨过刻度，向前校验一个刻度；
                    for (int i = 0; i < 2; i++) {
                        List<Integer> tmpData = ringData.remove( (nowSecond+60-i)%60 );
                        if (tmpData != null) {
                            ringItemData.addAll(tmpData);
                        }
                    }

                    // ring trigger
                    logger.debug(" xxl-job, time-ring beat : " + nowSecond + " = " + Arrays.asList(ringItemData) );
                    if (ringItemData.size() > 0) {
                        // do trigger
                        for (Integer jobId: ringItemData) {
                            // do trigger
                            TaskTriggerThread.trigger(Long.valueOf(jobId), TriggerTypeEnum.CRON, -1, null, null);
                        }
                        // clear
                        ringItemData.clear();
                    }
                } catch (Exception e) {
                    if (!ringThreadToStop) {
                        logger.error("xxl-job, JobScheduleHelper#ringThread error:{}", e);
                    }
                }
            }
            logger.info(" xxl-job, JobScheduleHelper#ringThread stop");
        });
        ringThread.setDaemon(true);
        ringThread.setName("xxl-job, admin JobScheduleHelper#ringThread");
        ringThread.start();
    }

    private void refreshNextValidTime(Task jobInfo, Date fromTime) throws Exception {
        Date nextValidTime = generateNextValidTime(jobInfo, fromTime);
        if (nextValidTime != null) {
            jobInfo.setLastTriggerTime(jobInfo.getNextTriggerTime());
            jobInfo.setNextTriggerTime(nextValidTime.getTime());
        } else {
            jobInfo.setTriggerStatus(TriggerStatus.DISABLE);
            jobInfo.setLastTriggerTime(0L);
            jobInfo.setNextTriggerTime(0L);
            logger.warn(" xxl-job, refreshNextValidTime fail for job: jobId={}, scheduleType={}, scheduleConf={}",
                    jobInfo.getId(), jobInfo.getScheduleType(), jobInfo.getScheduleConf());
        }
    }

    private void pushTimeRing(int ringSecond, int jobId){
        // push async ring
        List<Integer> ringItemData = ringData.computeIfAbsent(ringSecond, k -> new ArrayList<>());
        ringItemData.add(jobId);
        logger.debug(" xxl-job, schedule push time-ring : " + ringSecond + " = " + Arrays.asList(ringItemData) );
    }

    public void toStop(){

        // 1、stop schedule
        scheduleThreadToStop = true;
        try {
            TimeUnit.SECONDS.sleep(1);  // wait
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        if (scheduleThread.getState() != Thread.State.TERMINATED){
            // interrupt and wait
            scheduleThread.interrupt();
            try {
                scheduleThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

        // if has ring data
        boolean hasRingData = false;
        if (!ringData.isEmpty()) {
            for (int second : ringData.keySet()) {
                List<Integer> tmpData = ringData.get(second);
                if (tmpData!=null && tmpData.size()>0) {
                    hasRingData = true;
                    break;
                }
            }
        }
        if (hasRingData) {
            try {
                TimeUnit.SECONDS.sleep(8);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

        // stop ring (wait job-in-memory stop)
        ringThreadToStop = true;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        if (ringThread.getState() != Thread.State.TERMINATED){
            // interrupt and wait
            ringThread.interrupt();
            try {
                ringThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

        logger.info("xxl-job, JobScheduleHelper stop");
    }


    // ---------------------- tools ----------------------
    public static Date generateNextValidTime(Task jobInfo, Date fromTime) throws Exception {
        ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(jobInfo.getScheduleType().name(), null);
        if (ScheduleTypeEnum.CRON == scheduleTypeEnum) {
            return new CronExpression(jobInfo.getScheduleConf()).getNextValidTimeAfter(fromTime);
        } else if (ScheduleTypeEnum.FIX_RATE == scheduleTypeEnum /*|| ScheduleTypeEnum.FIX_DELAY == scheduleTypeEnum*/) {
            return new Date(fromTime.getTime() + Integer.parseInt(jobInfo.getScheduleConf())*1000 );
        }
        return null;
    }

}
