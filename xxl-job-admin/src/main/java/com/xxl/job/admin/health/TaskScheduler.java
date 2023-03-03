package com.xxl.job.admin.health;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import java.util.concurrent.ScheduledFuture;

/**
 * @author sweeter
 * @date 2020/02/29
 */
@Component
public class TaskScheduler extends ThreadPoolTaskScheduler {

    public ScheduledFuture<?> schedule(Runnable baseTask, String expression){
       return this.schedule(baseTask, new CronTrigger(expression));
    }

    public TaskScheduler() {
        this.setPoolSize(20);
        this.setThreadNamePrefix("xxl-job-pro-taskScheduler-");
        this.setWaitForTasksToCompleteOnShutdown(true);
        this.setAwaitTerminationSeconds(300);
        this.initialize();
    }
}
