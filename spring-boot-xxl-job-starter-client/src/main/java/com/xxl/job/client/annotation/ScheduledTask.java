package com.xxl.job.client.annotation;

import java.lang.annotation.*;

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
     * init handler, invoked when JobThread init
     */
    String init() default "";

    /**
     * destroy handler, invoked when JobThread destroy
     */
    String destroy() default "";

}
