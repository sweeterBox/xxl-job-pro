package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.entity.Application;
import com.xxl.job.admin.entity.Instance;
import com.xxl.job.admin.entity.Log;
import com.xxl.job.admin.entity.Task;
import com.xxl.job.admin.enums.*;
import com.xxl.job.admin.repository.ApplicationRepository;
import com.xxl.job.admin.repository.InstanceRepository;
import com.xxl.job.admin.repository.LogRepository;
import com.xxl.job.admin.repository.TaskRepository;
import com.xxl.job.enums.AutoRegistryType;
import com.xxl.job.enums.ExecutorBlockStrategy;
import com.xxl.job.model.HandleCallbackParam;
import com.xxl.job.model.InstanceRegistry;
import com.xxl.job.model.R;
import com.xxl.job.model.TaskRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author sweeter
 * @date 2023/01/25
 */
@Slf4j
@Service
public class ApiServiceImpl  {

    @Resource
    private InstanceRepository instanceRepository;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private ApplicationRepository applicationRepository;

    @Resource
    private LogRepository logRepository;

    public R<String> callback(List<HandleCallbackParam> callbackParamList) {

        for (HandleCallbackParam param: callbackParamList) {
            Optional<Log> logOpt = logRepository.findById(param.getLogId());

            if (logOpt.isPresent()) {
                Log en = logOpt.get();
                // avoid repeat callback, trigger child job etc
                if (en.getHandleStatus() > 0) {
                    return new R<>(R.FAIL_CODE, "en repeate callback.");
                }

                // handle msg
                StringBuilder handleMsg = new StringBuilder();
                if (en.getHandleContent()!=null) {
                    handleMsg.append(en.getHandleContent()).append("<br>");
                }
                if (param.getContent() != null) {
                    handleMsg.append(param.getContent());
                }

                // success, save log
                if (Objects.isNull(en.getHandleStartTime())) {
                    en.setHandleStartTime(param.getStartTime());
                }
                en.setHandleEndTime(param.getEndTime());

                en.setHandleStatus(param.getStatus());
                en.setHandleContent(handleMsg.toString());
                if (param.getStatus().compareTo(200) == 0 || param.getStatus().compareTo(0) == 0) {
                    en.setNotifyStatus(NotifyStatus.NOT);
                }else {
                    en.setNotifyStatus(NotifyStatus.TODO);
                }
                // text最大64kb 避免长度过长
                if (StringUtils.isNotBlank(en.getHandleContent()) && en.getHandleContent().length() > 15000) {
                    en.setHandleContent(en.getHandleContent().substring(0, 15000));
                }
                logRepository.save(en);
            }else {
                log.error("log[{}] item not found.", param.getLogId());
            }
        }
        return R.SUCCESS;
    }

    @Transactional
    public R<String> registry(InstanceRegistry registry) {
        if (StringUtils.isBlank(registry.getName())) {
            return new R<>(400, "Illegal Argument, name is null");
        }
        if (StringUtils.isBlank(registry.getUrl())) {
            return new R<>(400, "Illegal Argument,url is null");
        }
        Optional<Instance> instanceOpt = this.instanceRepository.findAllByNameAndUrl(registry.getName(), registry.getUrl());
        if (instanceOpt.isPresent()) {
            Instance instance = instanceOpt
                    .map(v -> {
                        v.setWeight(registry.getWeight());
                        v.setStatus(InstanceStatus.UP);
                        v.setHealthy(true);
                        v.setUpdateTime(LocalDateTime.now());
                        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(v.getUrl())
                                .build();
                        v.setHost(uriComponents.getHost());
                        v.setPort(uriComponents.getPort());
                        v.setSecure("https".equals(uriComponents.getScheme()));
                        return v;
                    }).get();
            this.instanceRepository.save(instance);
        }else {
            Instance instance = new Instance();
            BeanUtils.copyProperties(registry, instance);
            instance.setStatus(InstanceStatus.UP);
            instance.setHealthy(true);
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(instance.getUrl())
                    .build();
            instance.setHost(uriComponents.getHost());
            instance.setPort(uriComponents.getPort());
            instance.setSecure("https".equals(uriComponents.getScheme()));
            this.instanceRepository.save(instance);
        }
        if (!this.applicationRepository.existsByAndName(registry.getName())) {
            Application application = new Application();
            application.setTitle(registry.getTitle());
            application.setName(registry.getName());
            this.applicationRepository.save(application);
        }
        return R.SUCCESS;
    }

    @Transactional
    public R<String> deregister(InstanceRegistry registry) {
        Optional<Instance> instanceOpt = this.instanceRepository.findAllByNameAndUrl(registry.getName(), registry.getUrl());
        if (instanceOpt.isPresent()) {
            if (instanceOpt.map(Instance::getEphemeral).orElse(true)) {
                this.instanceRepository.delete(instanceOpt.get());
            }else {
                Instance instance = instanceOpt
                        .map(v -> {
                            v.setStatus(InstanceStatus.DOWN);
                            v.setHealthy(false);
                            v.setUpdateTime(LocalDateTime.now());
                            return v;
                        }).get();
                this.instanceRepository.save(instance);
            }
        }else {
          return new R<>(R.FAIL_CODE, "not found.");
        }
        return R.SUCCESS;
    }

    public R<String> saveTask(TaskRegistry taskRegistry) {
        //保存任务信息
        boolean exist = this.taskRepository.existsAllByExecutorHandler(taskRegistry.getName());
        Task task = null;
        if (exist) {
            if (AutoRegistryType.UPDATE.equals(taskRegistry.getAutoRegistry())) {
                task = this.taskRepository.findByExecutorHandler(taskRegistry.getName());
                task.setScheduleConf(taskRegistry.getCron());
                task.setDescription(taskRegistry.getDescription());
                this.taskRepository.save(task);
            }
        }else {
            if (AutoRegistryType.CREATE.equals(taskRegistry.getAutoRegistry())) {
                task = new Task();
                task.setApplicationName(taskRegistry.getApplicationName());
                task.setAuthor(taskRegistry.getAuthor());
                task.setMisfireStrategy(MisfireStrategy.DO_NOTHING);
                task.setExecutorBlockStrategy(ExecutorBlockStrategy.SERIAL_EXECUTION.name());
                task.setExecutorRouteStrategy(RouteStrategy.ROUND);
                task.setExecutorHandler(taskRegistry.getName());
                task.setDescription(taskRegistry.getDescription());
                task.setGlueType(GlueType.BEAN);
                task.setScheduleConf(taskRegistry.getCron());
                task.setScheduleType(ScheduleType.CRON);
                task.setTriggerStatus(taskRegistry.isAutoStart()?TriggerStatus.ENABLE:TriggerStatus.DISABLE);
                task.setExecutorFailRetryCount(0);
                this.taskRepository.save(task);
            }
        }

        return R.SUCCESS;
    }

}
