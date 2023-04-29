package com.xxl.job.client.annotation;

import com.xxl.job.enums.AutoRegistryType;
import java.lang.annotation.*;

/**
 * copy from com.xxl.job.core.handler.annotation.XxlJob
 * 该注解将在未来的版本中去除，请使用最新的注解com.xxl.job.client.annotation.ScheduledTask
 *
 * 如果spring bean对象的方法上标记有com.xxl.job.client.annotation.ScheduledTask注解，该方法就具备了任务调度能力
 * @author sweeter
 * @date 2023/2/25
 */
@Documented
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ScheduledTask {

    /**
     * 任务 name
     */
    String value() default "";

    /**
     * 中文描述信息
     */
    String description() default "UNKNOWN";

    /**
     *
     * @return
     */
    String author() default "anonymous";

    /**
     * CRON 表达式
     * @return
     */
    String cron() default "";

    /**
     * 初始化时启动任务
     * @return
     */
    boolean autoStart() default false;

    /**
     * 任务自动注册，当cron不为空时有效
     *
     * @return
     */
    AutoRegistryType autoRegistry() default AutoRegistryType.NONE;

    /**
     * init handler, invoked when JobThread init
     */
    String init() default "";

    /**
     * destroy handler, invoked when JobThread destroy
     */
    String destroy() default "";

}
