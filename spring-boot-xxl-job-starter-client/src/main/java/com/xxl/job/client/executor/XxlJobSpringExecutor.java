package com.xxl.job.client.executor;

import com.xxl.job.client.annotation.ScheduledTask;
import com.xxl.job.client.glue.GlueFactory;
import com.xxl.job.client.handler.AbstractScheduledTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * xxl-job executor (for spring)
 *
 * @author xuxueli 2018-11-01 09:24:52
 */
public class XxlJobSpringExecutor extends XxlJobExecutor implements ApplicationContextAware, SmartInitializingSingleton {

    private static final Logger logger = LoggerFactory.getLogger(XxlJobSpringExecutor.class);

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        XxlJobSpringExecutor.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        // init TaskHandler Repository
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);
        scheduledExecutorService.schedule(()->{
            try {
                this.initTaskHandlerRepository(applicationContext);
                // refresh GlueFactory
                GlueFactory.refreshInstance(1);
                super.start();
            } catch (Exception e) {
                logger.error("初始化失败：{}", e.getMessage());
            }
        }, 30, TimeUnit.SECONDS);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    private void initTaskHandlerRepository(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            return;
        }
        // init job handler from method
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            if (bean instanceof AbstractScheduledTask) {
                AbstractScheduledTask scheduledTask = (AbstractScheduledTask) bean;
                super.registryBeanHandler(scheduledTask);
            }else {
                // referred to ：org.springframework.context.event.EventListenerMethodProcessor.processBean
                Map<Method, ScheduledTask> annotatedMethods = null;
                try {
                    annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(), (MethodIntrospector.MetadataLookup<ScheduledTask>) method -> AnnotatedElementUtils.findMergedAnnotation(method, ScheduledTask.class));
                } catch (Throwable ex) {
                    logger.error("xxl-job method-jobhandler resolve error for bean[" + beanDefinitionName + "].", ex);
                }
                if (Objects.isNull(annotatedMethods) || annotatedMethods.isEmpty()) {
                    continue;
                }

                for (Map.Entry<Method, ScheduledTask> methodScheduledTaskEntry : annotatedMethods.entrySet()) {
                    Method executeMethod = methodScheduledTaskEntry.getKey();
                    ScheduledTask scheduledTask = methodScheduledTaskEntry.getValue();
                    super.registryMethodHandler(scheduledTask, bean, executeMethod);
                }
            }
        }
    }


}
