package com.xxl.job.admin.api;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.core.scheduler.MisfireStrategyEnum;
import com.xxl.job.admin.core.scheduler.ScheduleTypeEnum;
import com.xxl.job.admin.core.thread.JobScheduleHelper;
import com.xxl.job.admin.entity.Task;
import com.xxl.job.admin.enums.ScheduleType;
import com.xxl.job.admin.model.ReqExeOnce;
import com.xxl.job.admin.model.ReqTask;
import com.xxl.job.admin.model.TaskInfo;
import com.xxl.job.admin.query.TaskQuery;
import com.xxl.job.admin.service.TaskService;
import com.xxl.job.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * @author sweeter
 * @date 2022/9/11
 */
@Slf4j
@Api(tags = "任务(Task)")
@RestController
@RequestMapping("/v1.0/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @ApiOperation(value = "查询列表")
    @GetMapping("findPageList")
    public ResponseEntity<ResultPage<TaskInfo>> findPageList(TaskQuery query) {
        ResultPage<TaskInfo> resultPage = taskService.findPageList(query);
        return ResponseEntity.ok(resultPage);
    }


    @ApiOperation(value = "保存(Save)")
    @PostMapping("save")
    public ResponseEntity<Void> save(@Validated @RequestBody ReqTask reqTask) {
        log.info("/v1.0/task/save 入参{}", reqTask);
        reqTask.validate();
        Task task = new Task();
        BeanUtils.copyProperties(reqTask, task);
        taskService.save(task);
        return ResponseEntity.ok(null);
    }

    @ApiOperation(value = "删除(Delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        taskService.delete(id);
        return ResponseEntity.ok(null);
    }

    @ApiOperation(value = "停止任务")
    @PostMapping("stop/{id}")
    public ResponseEntity<Void> stop(@PathVariable("id") Long id) {
        taskService.stop(id);
        return ResponseEntity.ok(null);
    }

    @ApiOperation(value = "开始任务")
    @PostMapping("start/{id}")
    public ResponseEntity<Void> start(@PathVariable("id") Long id) {
        taskService.start(id);
        return ResponseEntity.ok(null);
    }

    @ApiOperation(value = "执行一次")
    @PostMapping("executeOnce")
    public ResponseEntity<Void> executeOnce(@RequestBody @Validated ReqExeOnce param) {
        taskService.executeOnce(param);
        return ResponseEntity.ok(null);
    }

    @ApiOperation(value = "下一次执行时间")
    @GetMapping("nextTriggerTime")
    public ResponseEntity<List<String>> nextTriggerTime(String scheduleType, String scheduleConf) {
        Task paramInfo = new Task();
        paramInfo.setScheduleType(ScheduleType.valueOf(scheduleType));
        paramInfo.setScheduleConf(scheduleConf);
        List<String> result = new ArrayList<>();
        try {
            Date lastTime = new Date();
            for (int i = 0; i < 5; i++) {
                lastTime = JobScheduleHelper.generateNextValidTime(paramInfo, lastTime);
                if (lastTime != null) {
                    result.add(DateUtil.formatDateTime(lastTime));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "查询客户端任务")
    @GetMapping("findClientTasks")
    public ResponseEntity<List<com.xxl.job.model.TaskInfo>> findClientTasks(@RequestParam String applicationName) {
        List<com.xxl.job.model.TaskInfo> tasks = this.taskService.tasks(applicationName);
        return ResponseEntity.ok(tasks);
    }


}
