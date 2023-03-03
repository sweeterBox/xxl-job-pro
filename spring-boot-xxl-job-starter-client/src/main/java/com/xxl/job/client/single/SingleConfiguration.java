package com.xxl.job.client.single;

import com.xxl.job.client.XxlJobProperties;
import com.xxl.job.client.task.InstanceRegistryThread;
import org.springframework.boot.CommandLineRunner;

/**
 * @author sweeter
 * @date 2023/3/2
 */
public class SingleConfiguration implements CommandLineRunner {

    private final XxlJobProperties xxlJobProperties;

    public SingleConfiguration(XxlJobProperties xxlJobProperties) {
        this.xxlJobProperties = xxlJobProperties;
    }

    @Override
    public void run(String... args) throws Exception {
        InstanceRegistryThread.getInstance().start(this.xxlJobProperties.getName(), this.xxlJobProperties.getAddress(), this.xxlJobProperties.getTitle());
    }
}
