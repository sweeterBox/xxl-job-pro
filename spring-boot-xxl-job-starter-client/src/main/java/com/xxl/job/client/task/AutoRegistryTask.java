package com.xxl.job.client.task;

import com.xxl.job.client.repository.ScheduledHandlerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author sweeter
 * @date 2023/3/12
 */
@Slf4j
@Component
public class AutoRegistryTask implements CommandLineRunner {

    private final ScheduledHandlerRepository scheduledHandlerRepository;

    public AutoRegistryTask(ScheduledHandlerRepository scheduledHandlerRepository) {
        this.scheduledHandlerRepository = scheduledHandlerRepository;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
