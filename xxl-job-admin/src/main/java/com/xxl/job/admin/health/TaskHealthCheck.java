package com.xxl.job.admin.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author sweeter
 * @date 2023/2/12
 */
@Slf4j
@Component
public class TaskHealthCheck {

    @Resource
    private TaskScheduler taskScheduler;

    public TaskHealthCheck() {

    }

    public void start(){
        taskScheduler.scheduleWithFixedDelay(() -> {
            //检查日志通知状态
            //触发通知

        }, Duration.ofSeconds(30L));

    }
}
