package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.common.jpa.query.QueryHandler;
import com.xxl.job.admin.core.Executor;
import com.xxl.job.admin.core.ExecutorHttpClient;
import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.core.cron.CronExpression;
import com.xxl.job.admin.core.scheduler.MisfireStrategyEnum;
import com.xxl.job.admin.core.scheduler.ScheduleTypeEnum;
import com.xxl.job.admin.core.thread.JobScheduleHelper;
import com.xxl.job.admin.core.thread.TaskTriggerThread;
import com.xxl.job.admin.core.trigger.TriggerTypeEnum;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.entity.Application;
import com.xxl.job.admin.entity.Instance;
import com.xxl.job.admin.entity.Task;
import com.xxl.job.admin.enums.InstanceStatus;
import com.xxl.job.admin.enums.TriggerStatus;
import com.xxl.job.admin.model.ReqExeOnce;
import com.xxl.job.admin.model.TaskInfo;
import com.xxl.job.admin.query.TaskQuery;
import com.xxl.job.admin.repository.*;
import com.xxl.job.admin.service.TaskService;
import com.xxl.job.enums.ExecutorBlockStrategy;
import com.xxl.job.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * @author sweeter
 * @date 2022/9/3
 */
@Transactional(rollbackFor = Exception.class,readOnly = true)
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskRepository taskRepository;


    @Resource
    public LogRepository logRepository;

    @Resource
    private LogGlueRepository logGlueRepository;

    @Resource
    private LogReportRepository logReportRepository;

    @Resource
    private ApplicationRepository applicationRepository;

    @Resource
    private InstanceRepository instanceRepository;


    @Override
    public Task getById(Long id) {
        return taskRepository.getOne(id);
    }

    @Override
    public ResultPage<TaskInfo> findPageList(TaskQuery query) {
        Page<Task> page = this.taskRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHandler.getPredicate(root, query, criteriaBuilder), query.createPageRequest());
        List<String> applicationNames = page.get().map(Task::getApplicationName).collect(Collectors.toList());
        List<Application> applicationList = this.applicationRepository.findAllByNameIn(applicationNames);
        Map<String, Application> applicationMaps = applicationList.stream().collect(Collectors.toMap(Application::getName, o -> o, (a, b) -> b));
        Page<TaskInfo> taskInfoPage = page.map(v -> {
            TaskInfo info = new TaskInfo();
            BeanUtils.copyProperties(v, info);
            Application application = applicationMaps.get(v.getApplicationName());
            if (Objects.nonNull(application)) {
                info.setApplicationName(application.getName());
            }
            return info;
        });
        return ResultPage.of(taskInfoPage);
    }

    /**
     * 保存任务
     *
     * @param task
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R<String> save(Task task) {
        // valid base
        if (Objects.isNull(task.getId())) {
            if (Objects.isNull(task.getExecutorTimeout())) {
                task.setExecutorTimeout(0L);
            }
            if (Objects.isNull(task.getTriggerStatus())) {
                task.setTriggerStatus(TriggerStatus.DISABLE);
            }
        }else {
            Task en = this.taskRepository.getOne(task.getId());
            if (Objects.nonNull(en)) {
                task.setTriggerStatus(en.getTriggerStatus());
                task.setGlueType(en.getGlueType());
                task.setExecutorHandler(en.getExecutorHandler());
            }
        }

        // valid trigger
        ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(task.getScheduleType().name(), null);
        if (scheduleTypeEnum == null) {
        }
        if (scheduleTypeEnum == ScheduleTypeEnum.CRON) {
            if (task.getScheduleConf() == null || !CronExpression.isValidExpression(task.getScheduleConf())) {
            }
        } else if (scheduleTypeEnum == ScheduleTypeEnum.FIX_RATE/* || scheduleTypeEnum == ScheduleTypeEnum.FIX_DELAY*/) {
            if (task.getScheduleConf() == null) {
            }
            try {
                int fixSecond = Integer.valueOf(task.getScheduleConf());
                if (fixSecond < 1) {
                 //   return new R<>(R.FAIL_CODE, (I18nUtil.getString("schedule_type") + I18nUtil.getString("system_unvalid")));
                }
            } catch (Exception e) {
               // return new R<>(R.FAIL_CODE, (I18nUtil.getString("schedule_type") + I18nUtil.getString("system_unvalid")));
            }
        }

        // valid advanced
        if (task.getExecutorRouteStrategy() == null) {
            // return new R<>(R.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorRouteStrategy") + I18nUtil.getString("system_unvalid")));
        }
        if (MisfireStrategyEnum.match(task.getMisfireStrategy().name(), null) == null) {
            //  return new R<>(R.FAIL_CODE, (I18nUtil.getString("misfire_strategy")+I18nUtil.getString("system_unvalid")) );
        }
        if (ExecutorBlockStrategy.match(task.getExecutorBlockStrategy(), null) == null) {
            //  return new R<>(R.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorBlockStrategy")+I18nUtil.getString("system_unvalid")) );
        }

        // 》ChildJobId valid
        if (task.getChildJobId() != null && task.getChildJobId().trim().length() > 0) {
            String[] childJobIds = task.getChildJobId().split(",");
            for (String childJobIdItem : childJobIds) {
                if (childJobIdItem != null && childJobIdItem.trim().length() > 0 && isNumeric(childJobIdItem)) {
                    Task childJobInfo = taskRepository.findById(Long.valueOf(childJobIdItem)).get();
                    if (childJobInfo == null) {
                        return new R<String>(R.FAIL_CODE,
                                MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId") + "({0})" + I18nUtil.getString("system_not_found")), childJobIdItem));
                    }
                } else {
                    return new R<>(R.FAIL_CODE,
                            MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId") + "({0})" + I18nUtil.getString("system_unvalid")), childJobIdItem));
                }
            }

            // join , avoid "xxx,,"
            String temp = "";
            for (String item : childJobIds) {
                temp += item + ",";
            }
            temp = temp.substring(0, temp.length() - 1);

            task.setChildJobId(temp);
        }

        // add in db
        taskRepository.save(task);
        if (task.getId() < 1) {
            return new R<>(R.FAIL_CODE, (I18nUtil.getString("jobinfo_field_add") + I18nUtil.getString("system_fail")));
        }

        return new R<>(String.valueOf(task.getId()));
    }

    /**
     * 删除任务
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R<String> delete(Long id) {
        Optional<Task> xxlJobInfoOpt = taskRepository.findById(id);
        if (xxlJobInfoOpt.isPresent()) {
            taskRepository.deleteById(id);
            logRepository.deleteByTaskId(id);
            logGlueRepository.deleteByTaskId(id);
        }
        return R.SUCCESS;
    }

    /**
     * 停止任务
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R<String> stop(Long id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setTriggerStatus(TriggerStatus.DISABLE);
            task.setLastTriggerTime(0L);
            task.setNextTriggerTime(0L);
            taskRepository.save(task);
            return R.SUCCESS;
        }
        return R.FAIL;
    }

    /**
     * 开始任务
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R<String> start(Long id) {
        Task info = taskRepository.findById(id).orElse(null);

        // valid
        ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(info.getScheduleType().name(), ScheduleTypeEnum.NONE);
        if (ScheduleTypeEnum.NONE == scheduleTypeEnum) {
            return new R<>(R.FAIL_CODE, (I18nUtil.getString("schedule_type_none_limit_start")) );
        }
        // next trigger time (5s后生效，避开预读周期)
        long nextTriggerTime = 0;
        try {
            Date nextValidTime = JobScheduleHelper.generateNextValidTime(info, new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
            if (nextValidTime == null) {
                return new R<>(R.FAIL_CODE, (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_unvalid")) );
            }
            nextTriggerTime = nextValidTime.getTime();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new R<>(R.FAIL_CODE, (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_unvalid")) );
        }
        info.setTriggerStatus(TriggerStatus.ENABLE);
        info.setLastTriggerTime(0L);
        info.setNextTriggerTime(nextTriggerTime);
        taskRepository.save(info);
        return R.SUCCESS;
    }

    /**
     * 执行一次
     *
     * @param param
     */
    @Override
    public void executeOnce(ReqExeOnce param) {
        TaskTriggerThread.trigger(param.getId(), TriggerTypeEnum.MANUAL, -1, null, param.getExecutorParam());
    }

    /**
     * 查询客户端任务
     *
     * @param applicationName
     * @return
     */
    @Override
    public List<com.xxl.job.model.TaskInfo> tasks(String applicationName) {
        List<Instance> instances = this.instanceRepository.findByNameAndStatus(applicationName, InstanceStatus.UP);
        Optional<String> clientUrlOpt = instances.stream().findAny().map(Instance::getUrl);
        if (clientUrlOpt.isPresent()) {
            Executor executor = new ExecutorHttpClient(clientUrlOpt.get(), XxlJobAdminConfig.getAdminConfig().getAccessToken());
            return Optional.ofNullable(executor.tasks()).map(R::getContent).orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }

}
