package com.xxl.job.client.executor;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.xxl.job.client.executor.client.AdminApiClient;
import com.xxl.job.client.annotation.ScheduledTask;
import com.xxl.job.client.handler.AbstractScheduledTask;
import com.xxl.job.client.handler.BeanHandler;
import com.xxl.job.client.handler.MethodHandler;
import com.xxl.job.client.executor.impl.DefaultExecutor;
import com.xxl.job.client.executor.server.ClientServer;
import com.xxl.job.client.repository.ScheduledHandlerRepository;
import com.xxl.job.client.repository.ScheduledThreadRepository;
import com.xxl.job.client.task.TriggerCallbackThread;
import com.xxl.job.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;

import javax.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * Created by xuxueli on 2016/3/2 21:14.
 */
public abstract class XxlJobExecutor {

    private static final Logger log = LoggerFactory.getLogger(XxlJobExecutor.class);

    private String adminAddresses;

    private String accessToken;

    private String clientUrl;

    private String name;

    private String title;

    private String ip;

    private Integer port;

    private String logPath;

    private int logRetentionDays;

    private ScheduledHandlerRepository jobHandlerRepository;

    private ScheduledThreadRepository jobThreadRepository;

    private Executor executor = new DefaultExecutor(this.jobHandlerRepository, this.jobThreadRepository);


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public void setAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public void setPort(Integer port) {
        this.port = port;
    }
    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }
    public void setLogRetentionDays(int logRetentionDays) {
        this.logRetentionDays = logRetentionDays;
    }

    public ScheduledHandlerRepository getJobHandlerRepository() {
        return jobHandlerRepository;
    }

    public void setJobHandlerRepository(ScheduledHandlerRepository jobHandlerRepository) {
        this.jobHandlerRepository = jobHandlerRepository;
    }

    public ScheduledThreadRepository getJobThreadRepository() {
        return jobThreadRepository;
    }

    public void setJobThreadRepository(ScheduledThreadRepository jobThreadRepository) {
        this.jobThreadRepository = jobThreadRepository;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }


    public void start() {
        // init TriggerCallbackThread
        TriggerCallbackThread.getInstance().start();

        // init executor-server
        initEmbedServer(port, executor);
    }

    @PreDestroy
    public void destroy(){
        // destroy executor-server
        stopEmbedServer();

        jobThreadRepository.destroy();

        jobHandlerRepository.clear();
        // destroy TriggerCallbackThread
        TriggerCallbackThread.getInstance().toStop();

    }


    public static List<AdminApiClient> getAdminBizList(){
        return Lists.newArrayList(SpringContextUtils.getBean(AdminApiClient.class));
    }

    // ---------------------- executor-server (rpc provider) ----------------------
    private ClientServer embedServer = null;

    private void initEmbedServer(Integer port,Executor executor) {

        // start
        embedServer = new ClientServer();
        embedServer.start(port, executor);
    }

    private void stopEmbedServer() {
        // stop provider factory
        if (embedServer != null) {
            try {
                embedServer.stop();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    protected void registryMethodHandler(ScheduledTask scheduledTask, Object bean, Method executeMethod){
        if (scheduledTask == null) {
            log.error("ScheduledTask is null");
            return;
        }

        String name = scheduledTask.value();
        String description = scheduledTask.description();
        String author = scheduledTask.author();
        //make and simplify the variables since they'll be called several times later
        Class<?> clazz = bean.getClass();
        String methodName = executeMethod.getName();
        if (name.trim().length() == 0) {
            throw new RuntimeException("xxl-job method-jobhandler name invalid, for[" + clazz + "#" + methodName + "] .");
        }
        if (jobHandlerRepository.findOne(name) != null) {
            throw new RuntimeException("xxl-job jobhandler[" + name + "(" + description + ")] naming conflicts.");
        }
        executeMethod.setAccessible(true);
        boolean deprecated = AnnotatedElementUtils.hasAnnotation(executeMethod, Deprecated.class);
        // init and destroy
        Method initMethod = null;
        Method destroyMethod = null;

        if (scheduledTask.init().trim().length() > 0) {
            try {
                initMethod = clazz.getDeclaredMethod(scheduledTask.init());
                initMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("xxl-job method-jobhandler initMethod invalid, for[" + clazz + "#" + methodName + "] .");
            }
        }
        if (scheduledTask.destroy().trim().length() > 0) {
            try {
                destroyMethod = clazz.getDeclaredMethod(scheduledTask.destroy());
                destroyMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("xxl-job method-jobhandler destroyMethod invalid, for[" + clazz + "#" + methodName + "] .");
            }
        }
        this.jobHandlerRepository.save(name, new MethodHandler(bean, executeMethod, initMethod, destroyMethod, name, description, deprecated, author));
    }


    protected void registryBeanHandler(AbstractScheduledTask scheduledTask) {
        this.jobHandlerRepository.save(scheduledTask.name(), new BeanHandler(scheduledTask));
    }

}
