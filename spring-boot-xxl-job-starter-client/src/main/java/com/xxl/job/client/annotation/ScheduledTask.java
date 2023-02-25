package com.xxl.job.client.annotation;

import java.lang.annotation.*;

/**
 * copy from com.xxl.job.core.handler.annotation.XxlJob
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
     * init handler, invoked when JobThread init
     */
    String init() default "";

    /**
     * destroy handler, invoked when JobThread destroy
     */
    String destroy() default "";

}
