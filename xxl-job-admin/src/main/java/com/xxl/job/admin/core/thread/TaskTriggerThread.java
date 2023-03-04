package com.xxl.job.admin.core.thread;

import com.xxl.job.admin.core.trigger.TriggerTypeEnum;
import com.xxl.job.admin.core.trigger.XxlJobTrigger;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * job trigger thread pool helper
 *
 * @author xuxueli 2018-07-03 21:08:07
 */
@Slf4j
public class TaskTriggerThread {

    // ---------------------- trigger pool ----------------------

    // fast/slow thread pool
    private ThreadPoolExecutor fastTriggerPool = null;

    private ThreadPoolExecutor slowTriggerPool = null;

    public void start(){
       // SystemProperties systemProperties = SpringContextUtils.getBean(SystemProperties.class);

        fastTriggerPool = new ThreadPoolExecutor(
                10,
               200,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000),
                r -> new Thread(r, "xxl-job, admin TaskTriggerThread-fastTriggerPool-" + r.hashCode()));

        slowTriggerPool = new ThreadPoolExecutor(
                10,
               100,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(2000),
                r -> new Thread(r, "xxl-job, admin TaskTriggerThread-slowTriggerPool-" + r.hashCode()));
    }


    public void stop() {
        fastTriggerPool.shutdownNow();
        slowTriggerPool.shutdownNow();
        log.info("xxl-job admin TaskTriggerThread shutdown success.");
    }


    // job timeout count // ms > min
    private volatile long minTim = System.currentTimeMillis()/60000;

    private volatile ConcurrentMap<Integer, AtomicInteger> jobTimeoutCountMap = new ConcurrentHashMap<>();


    /**
     * add trigger
     */
    public void addTrigger(final Long jobId,
                           final TriggerTypeEnum triggerType,
                           final int failRetryCount,
                           final String executorShardingParam,
                           final String executorParam) {

        // choose thread pool
        ThreadPoolExecutor triggerPool_ = fastTriggerPool;
        AtomicInteger jobTimeoutCount = jobTimeoutCountMap.get(jobId);
        if (jobTimeoutCount!=null && jobTimeoutCount.get() > 10) {      // job-timeout 10 times in 1 min
            triggerPool_ = slowTriggerPool;
        }

        // trigger
        triggerPool_.execute(() -> {

            long start = System.currentTimeMillis();

            try {
                // do trigger 执行任务调度
                XxlJobTrigger.trigger(jobId, triggerType, failRetryCount, executorShardingParam, executorParam);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {

                // check timeout-count-map
                long minTim_now = System.currentTimeMillis()/60000;
                if (minTim != minTim_now) {
                    minTim = minTim_now;
                    jobTimeoutCountMap.clear();
                }

                // incr timeout-count-map
                long cost = System.currentTimeMillis()-start;
                if (cost > 500) {       // ob-timeout threshold 500ms
                    AtomicInteger timeoutCount = jobTimeoutCountMap.putIfAbsent(jobId.intValue(), new AtomicInteger(1));
                    if (timeoutCount != null) {
                        timeoutCount.incrementAndGet();
                    }
                }

            }

        });
    }



    // ---------------------- helper ----------------------

    private static TaskTriggerThread helper = new TaskTriggerThread();

    public static void toStart() {
        helper.start();
    }
    public static void toStop() {
        helper.stop();
    }

    /**
     * @param jobId
     * @param triggerType
     * @param failRetryCount
     * 			>=0: use this param
     * 			<0: use param from job info config
     * @param executorShardingParam
     * @param executorParam
     *          null: use job param
     *          not null: cover job param
     */
    public static void trigger(Long jobId, TriggerTypeEnum triggerType, int failRetryCount, String executorShardingParam, String executorParam) {
        helper.addTrigger(jobId, triggerType, failRetryCount, executorShardingParam, executorParam);
    }

}
