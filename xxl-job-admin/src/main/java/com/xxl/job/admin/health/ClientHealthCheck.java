package com.xxl.job.admin.health;

import com.xxl.job.admin.core.Executor;
import com.xxl.job.admin.core.ExecutorHttpClient;
import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.entity.Instance;
import com.xxl.job.admin.enums.InstanceStatus;
import com.xxl.job.admin.repository.InstanceRepository;
import com.xxl.job.model.R;
import com.xxl.job.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author sweeter
 * @date 2023/2/12
 */
@Slf4j
@Component
public class ClientHealthCheck implements CommandLineRunner {

    @Resource
    private TaskScheduler taskScheduler;

    public ClientHealthCheck() {

    }

    public void start(){
        taskScheduler.scheduleWithFixedDelay(() -> {
            InstanceRepository instanceRepository = SpringContextUtils.getBean(InstanceRepository.class);
            LocalDateTime time = LocalDateTime.now().minusMinutes(1L);
            Specification<Instance> specification = (root, criteriaQuery, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.conjunction();
                predicate.getExpressions().add(criteriaBuilder.lessThan(root.get("updateTime"), time));
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("status"), InstanceStatus.UP));
                return predicate;
            };
            List<Instance> instances = instanceRepository.findAll(specification);
            for (Instance instance : instances) {
                Executor executor = new ExecutorHttpClient(instance.getUrl(), XxlJobAdminConfig.getAdminConfig().getAccessToken());
                Optional<R<String>> resOpt = Optional.ofNullable(executor.beat())
                        .filter(v -> v.getCode() == 200);
                if (resOpt.isPresent()) {
                    log.info("{}-{} 健康检查", instance.getName(), instance.getUrl());
                } else {
                    //下线处理
                    log.warn("{}-{} 下线", instance.getName(), instance.getUrl());
                    if (instance.getEphemeral()) {
                        instanceRepository.delete(instance);
                    }else {
                        instance.setStatus(InstanceStatus.DOWN);
                        instance.setUpdateTime(LocalDateTime.now());
                        instanceRepository.save(instance);
                    }

                }
            }
        }, Duration.ofMinutes(1L));
    }

    @Override
    public void run(String... args) throws Exception {
        this.start();
    }
}
