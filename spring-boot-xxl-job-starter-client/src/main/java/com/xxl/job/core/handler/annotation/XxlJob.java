package com.xxl.job.core.handler.annotation;

import com.xxl.job.client.annotation.ScheduledTask;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * please use @com.xxl.job.client.annotation.ScheduledTask()
 *
 * annotation for method jobhandler
 *
 * @author xuxueli 2019-12-11 20:50:13
 */
@Deprecated
@ScheduledTask()
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XxlJob {

    /**
     * jobhandler name
     */
    @AliasFor(attribute = "value",annotation = ScheduledTask.class)
    String value();

    /**
     * init handler, invoked when JobThread init
     */
    @AliasFor(attribute = "init",annotation = ScheduledTask.class )
    String init() default "";

    /**
     * destroy handler, invoked when JobThread destroy
     */
    @AliasFor(attribute = "destroy",annotation = ScheduledTask.class )
    String destroy() default "";

}
