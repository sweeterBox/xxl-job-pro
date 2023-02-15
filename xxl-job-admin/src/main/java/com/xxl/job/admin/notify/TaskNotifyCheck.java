package com.xxl.job.admin.notify;

import com.xxl.job.admin.entity.Log;
import com.xxl.job.admin.entity.Task;
import com.xxl.job.admin.enums.NotifyStatus;
import com.xxl.job.admin.health.TaskScheduler;
import com.xxl.job.admin.repository.LogRepository;
import com.xxl.job.admin.repository.TaskRepository;
import com.xxl.job.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author sweeter
 * @date 2023/2/12
 */
@Slf4j
@Component
public class TaskNotifyCheck implements CommandLineRunner {

    @Resource
    private TaskScheduler taskScheduler;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private LogRepository logRepository;

    private final ObjectProvider<List<Notifier>> otherNotifiers;

    private CompositeNotifier compositeNotifier;

    public TaskNotifyCheck(ObjectProvider<List<Notifier>> otherNotifiers) {
        this.otherNotifiers = otherNotifiers;
        this.compositeNotifier = new CompositeNotifier(this.otherNotifiers.getIfAvailable(Collections::emptyList));
    }


    public void start(){
        taskScheduler.scheduleWithFixedDelay(() -> {
            //检查日志通知状态
            Specification<Log> specification = (root, criteriaQuery, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.conjunction();
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("notifyStatus"), NotifyStatus.TODO));
                return predicate;
            };
            Page<Log> pageList = logRepository.findAll(specification, PageRequest.of(0, 10, Sort.by("Id")));
            for (Log en : pageList.getContent()) {
                this.doNotify(en);
            }
        }, Duration.ofSeconds(5L));

    }

    private void doNotify(Log en) {
        Task task = taskRepository.findById(en.getTaskId()).get();
        TaskEvent taskEvent = new TaskEvent();
        TaskEvent.Task taskInfo = new TaskEvent.Task();
        TaskEvent.Log logInfo = new TaskEvent.Log();
        BeanUtils.copyProperties(task,taskInfo);
        BeanUtils.copyProperties(en,logInfo);
        taskEvent.setTimestamp(Instant.now());
        taskEvent.setId(UUID.randomUUID().toString().replace("-", ""));
        taskEvent.setLog(logInfo);
        taskEvent.setTask(taskInfo);
        try {
            compositeNotifier.notify(taskEvent);
            log.info("触发通知,{}", JsonUtils.obj2Json(taskEvent));
            en.setNotifyStatus(NotifyStatus.NOTIFIED);
        } catch (Exception e) {
            en.setNotifyStatus(NotifyStatus.FAIL);
            e.printStackTrace();
        }
        logRepository.save(en);
    }


    @Override
    public void run(String... args) throws Exception {
        this.start();
    }
}
