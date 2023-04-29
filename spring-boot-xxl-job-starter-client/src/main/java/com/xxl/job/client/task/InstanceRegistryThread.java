package com.xxl.job.client.task;

import com.xxl.job.client.enums.RegistryConfig;
import com.xxl.job.client.executor.client.AdminApiClient;
import com.xxl.job.client.executor.XxlJobExecutor;
import com.xxl.job.client.executor.model.InstanceRegistry;
import com.xxl.job.model.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuxueli on 17/3/2.
 */
public class InstanceRegistryThread {

    private static Logger log = LoggerFactory.getLogger(InstanceRegistryThread.class);

    private static InstanceRegistryThread instance = new InstanceRegistryThread();

    public static InstanceRegistryThread getInstance(){
        return instance;
    }

    private Thread registryThread;

    private volatile boolean toStop = false;

    public void start(final String name, final String address,final String title){
        toStop = false;
        // valid
        if (name==null || name.trim().length()==0) {
            log.warn("xxl-job client start fail, name is null.");
            return;
        }
        if (XxlJobExecutor.getAdminBizList() == null) {
            log.warn("xxl-job client start fail, adminAddresses is null.");
            return;
        }

        registryThread = new Thread(() -> {

            // registry
            while (!toStop) {
                try {
                    InstanceRegistry registryParam = new InstanceRegistry(name, address,title);
                    for (AdminApiClient adminBiz: XxlJobExecutor.getAdminBizList()) {
                        try {
                            R<String> registryResult = adminBiz.register(registryParam);
                            if (registryResult!=null && R.SUCCESS_CODE == registryResult.getCode()) {
                                registryResult = R.SUCCESS;
                                log.info("xxl-job instance registry success, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                                break;
                            } else {
                                log.error("xxl-job instance registry fail, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                            }
                        } catch (Exception e) {
                            log.error("xl-job instance registry error, registryParam:{}", registryParam, e);
                        }
                    }
                } catch (Exception e) {
                    if (!toStop) {
                        log.error(e.getMessage(), e);
                    }

                }

                try {
                    if (!toStop) {
                        TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
                    }
                } catch (InterruptedException e) {
                    if (!toStop) {
                        log.warn("xxl-job instance registry thread interrupted, error msg:{}", e.getMessage());
                    }
                }
            }

            // registry remove
            try {
                InstanceRegistry registryParam = new InstanceRegistry(name, address, title);
                for (AdminApiClient adminBiz: XxlJobExecutor.getAdminBizList()) {
                    try {
                        R<String> registryResult = adminBiz.deregister(registryParam);
                        if (registryResult != null && R.SUCCESS_CODE == registryResult.getCode()) {
                            registryResult = R.SUCCESS;
                            log.info("xxl-job instance registry-remove success, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                            break;
                        } else {
                            log.error("xxl-job instance registry-remove fail, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                        }
                    } catch (Exception e) {
                        if (!toStop) {
                            log.error("xxl-job registry-remove error, registryParam:{}", registryParam, e);
                        }
                    }

                }
            } catch (Exception e) {
                if (!toStop) {
                    log.error(e.getMessage(), e);
                }
            }
            log.info("xxl-job instance registry thread destroy.");
        });
        registryThread.setDaemon(true);
        registryThread.setName("xxl-job-pro-instance-registry");
        registryThread.start();
    }

    public void toStop() {
        toStop = true;
        if (registryThread != null && registryThread.isAlive()) {
            log.warn("线程{}-{} isAlive：{}", registryThread.getId(), registryThread.getName(), registryThread.isAlive());
            registryThread.interrupt();
            try {
                registryThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
