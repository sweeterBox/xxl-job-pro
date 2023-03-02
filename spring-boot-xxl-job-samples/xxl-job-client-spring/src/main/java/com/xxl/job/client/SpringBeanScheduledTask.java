package com.xxl.job.client;

import com.xxl.job.client.handler.AbstractScheduledTask;
import org.springframework.stereotype.Component;

/**
 * @author sweeter
 * @date 2023/2/19
 */
@Deprecated
@Component
public class SpringBeanScheduledTask extends AbstractScheduledTask {
    /**
     * 作者
     *
     * @return
     */
    @Override
    public String author() {
        return "作者";
    }

    /**
     * 任务中文描述信息
     *
     * @return 任务中文描述信息
     */
    @Override
    public String description() {
        return "Bean任务测试";
    }

    /**
     * 任务名称/标识（英文）
     *
     * @return task name
     */
    @Override
    public String name() {
       // return "SpringBeanScheduledTask";
        return "MyScheduledTask";
    }

    /**
     * 执行
     */
    @Override
    public void execute() {

        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    }
}
