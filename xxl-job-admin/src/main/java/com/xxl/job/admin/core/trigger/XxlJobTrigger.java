package com.xxl.job.admin.core.trigger;

import com.xxl.job.admin.enums.NotifyStatus;
import com.xxl.job.utils.SpringContextUtils;
import com.xxl.job.admin.entity.Instance;
import com.xxl.job.admin.entity.Task;
import com.xxl.job.admin.entity.Log;
import com.xxl.job.admin.core.scheduler.XxlJobScheduler;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.enums.InstanceStatus;
import com.xxl.job.admin.enums.RouteStrategy;
import com.xxl.job.admin.repository.InstanceRepository;
import com.xxl.job.admin.service.LogService;
import com.xxl.job.admin.service.TaskService;
import com.xxl.job.admin.core.Executor;
import com.xxl.job.enums.ExecutorBlockStrategy;
import com.xxl.job.model.R;
import com.xxl.job.utils.IpUtil;
import com.xxl.job.utils.ThrowableUtil;
import com.xxl.job.model.TriggerParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * xxl-job trigger
 * Created by xuxueli on 17/7/13.
 */
@Slf4j
public class XxlJobTrigger {

    /**
     * trigger job
     *
     * @param taskId
     * @param triggerType
     * @param failRetryCount
     * 			>=0: use this param
     * 			<0: use param from job info config
     * @param executorShardingParam
     * @param executorParam
     *          null: use job param
     *          not null: cover job param
     */
    public static void trigger(Long taskId, TriggerTypeEnum triggerType, int failRetryCount, String executorShardingParam, String executorParam) {
        TaskService infoService = SpringContextUtils.getBean(TaskService.class);
        Task task = infoService.getById(taskId);
        if (task == null) {
            log.warn("trigger fail, taskId invalid，taskId={}", taskId);
            return;
        }
        if (executorParam != null) {
            task.setExecutorParam(executorParam);
        }
        int finalFailRetryCount = failRetryCount >= 0 ? failRetryCount : task.getExecutorFailRetryCount();
        List<Instance> instances = SpringContextUtils.getBean(InstanceRepository.class).findByNameAndStatus(task.getApplicationName(), InstanceStatus.UP);
        List<String> instanceUrls = instances.stream().map(Instance::getUrl).distinct().collect(Collectors.toList());
        // sharding param
        int[] shardingParam = null;
        if (executorShardingParam != null) {
            String[] shardingArr = executorShardingParam.split("/");
            if (shardingArr.length == 2 && isNumeric(shardingArr[0]) && isNumeric(shardingArr[1])) {
                shardingParam = new int[2];
                shardingParam[0] = Integer.parseInt(shardingArr[0]);
                shardingParam[1] = Integer.parseInt(shardingArr[1]);
            }
        }
        if (RouteStrategy.SHARDING_BROADCAST.equals(task.getExecutorRouteStrategy())
                && !CollectionUtils.isEmpty(instanceUrls)
                && shardingParam == null) {
            for (int i = 0; i < instanceUrls.size(); i++) {
                processTrigger(instanceUrls, task, finalFailRetryCount, triggerType, i, instanceUrls.size());
            }
        } else {
            if (shardingParam == null) {
                shardingParam = new int[]{0, 1};
            }
            processTrigger(instanceUrls, task, finalFailRetryCount, triggerType, shardingParam[0], shardingParam[1]);
        }

    }

    private static boolean isNumeric(String str){
        try {
            int result = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @param instanceUrls 实例地址
     * @param task 任务
     * @param finalFailRetryCount 重试次数
     * @param triggerType
     * @param index                     sharding index
     * @param total                     sharding index
     */
    private static void processTrigger(List<String> instanceUrls, Task task, int finalFailRetryCount, TriggerTypeEnum triggerType, int index, int total){

        // block strategy
        ExecutorBlockStrategy blockStrategy = ExecutorBlockStrategy.match(task.getExecutorBlockStrategy(), ExecutorBlockStrategy.SERIAL_EXECUTION);
        // route strategy
        RouteStrategy executorRouteStrategyEnum = task.getExecutorRouteStrategy();
        String shardingParam = (RouteStrategy.SHARDING_BROADCAST.equals(executorRouteStrategyEnum) ? String.valueOf(index).concat("/").concat(String.valueOf(total)) : null);

        LogService logService = SpringContextUtils.getBean(LogService.class);
        // 1、save log-id
        Log taskLog = new Log();
        taskLog.setApplicationName(task.getApplicationName());
        taskLog.setTaskId(task.getId());
        taskLog.setTriggerTime(LocalDateTime.now());
        taskLog.setNotifyStatus(NotifyStatus.NOT);
        taskLog.setExecutorFailRetryCount(0);
        taskLog.setHandleStatus(0);
        logService.save(taskLog);
        log.debug("xxl-job trigger start, taskId:{}", taskLog.getId());

        // 2、init trigger-param
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setTaskId(task.getId());
        triggerParam.setExecutorHandler(task.getExecutorHandler());
        triggerParam.setExecutorParams(task.getExecutorParam());
        triggerParam.setExecutorBlockStrategy(task.getExecutorBlockStrategy());
        triggerParam.setExecutorTimeout(task.getExecutorTimeout());
        triggerParam.setLogId(taskLog.getId());
        triggerParam.setLogDateTime(taskLog.getTriggerTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        triggerParam.setGlueType(task.getGlueType().name());
        triggerParam.setGlueSource(task.getGlueSource());
        if (Objects.nonNull(task.getGlueUpdatetime())) {
            triggerParam.setGlueUpdatetime(task.getGlueUpdatetime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        triggerParam.setBroadcastIndex(index);
        triggerParam.setBroadcastTotal(total);

        // 3、init clientUrl
        String address = null;
        R<String> routeAddressResult = null;
        if (!CollectionUtils.isEmpty(instanceUrls)) {
            if (RouteStrategy.SHARDING_BROADCAST.equals(executorRouteStrategyEnum)) {
                if (index < instanceUrls.size()) {
                    address = instanceUrls.get(index);
                } else {
                    address = instanceUrls.get(0);
                }
            } else {
                routeAddressResult = executorRouteStrategyEnum.getRouter().route(triggerParam, instanceUrls);
                if (routeAddressResult.getCode() == R.SUCCESS_CODE) {
                    address = routeAddressResult.getContent();
                }
            }
        } else {
            log.error("No instances available for xxl-job-client");
            routeAddressResult = new R<>(R.FAIL_CODE, "No instances available for xxl-job-client");
        }

        // 4、trigger remote executor
        R<String> triggerResult = address != null ? runExecutor(triggerParam, address) : new R<>(R.FAIL_CODE, null);


        // 5、collection trigger info
        StringBuffer triggerMsgSb = new StringBuffer();
        triggerMsgSb.append(I18nUtil.getString("jobconf_trigger_type")).append("：").append(triggerType.getTitle());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_admin_adress")).append("：").append(IpUtil.getIp());;
        triggerMsgSb.append("<br>").append("客户端地址").append("：").append(instanceUrls);
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorRouteStrategy")).append("：").append(executorRouteStrategyEnum);
        if (shardingParam != null) {
            triggerMsgSb.append("("+shardingParam+")");
        }
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorBlockStrategy")).append("：").append(blockStrategy.getTitle());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_timeout")).append("：").append(task.getExecutorTimeout());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorFailRetryCount")).append("：").append(finalFailRetryCount);

        triggerMsgSb.append("<br><br><span style=\"color:#00c0ef;\" > " +
                "调度信息:" +
                "</span><br>")
                .append((routeAddressResult != null && routeAddressResult.getMsg() != null) ? routeAddressResult.getMsg() +
                        "<br><br>" : "").append(triggerResult.getMsg() != null ? triggerResult.getMsg() : "");

        // 6、save log trigger-info
        taskLog.setInstanceUrl(address);
        taskLog.setExecutorHandler(task.getExecutorHandler());
        taskLog.setExecutorParam(task.getExecutorParam());
        taskLog.setExecutorShardingParam(shardingParam);
        taskLog.setExecutorFailRetryCount(finalFailRetryCount);
        //jobLog.setTriggerTime();
        taskLog.setTriggerStatus(triggerResult.getCode());
        if (triggerResult.getCode()==200 || triggerResult.getCode() == 0) {
            taskLog.setNotifyStatus(NotifyStatus.NOT);
        }else {
            taskLog.setNotifyStatus(NotifyStatus.TODO);
        }
        taskLog.setTriggerContent(triggerMsgSb.toString());
        logService.save(taskLog);
        log.debug(" xxl-job trigger end, taskId:{}", taskLog.getId());
    }

    /**
     * run executor
     * @param triggerParam
     * @param clientUrl
     * @return
     */
    public static R<String> runExecutor(TriggerParam triggerParam, String clientUrl){
        R<String> runResult;
        try {
            Executor executor = XxlJobScheduler.getExecutorBiz(clientUrl);
            runResult = executor.run(triggerParam);
        } catch (Exception e) {
            log.error("xxl-job trigger error, please check if the executor[{}] is running.", clientUrl, e);
            runResult = new R<>(R.FAIL_CODE, ThrowableUtil.toString(e));
        }

        StringBuffer runResultSB = new StringBuffer(I18nUtil.getString("jobconf_trigger_run") + "：");
        runResultSB.append("<br>clientUrl：").append(clientUrl);
        runResultSB.append("<br>code：").append(runResult.getCode());
        runResultSB.append("<br>msg：").append(runResult.getMsg());
        runResult.setMsg(runResultSB.toString());
        return runResult;
    }

}
