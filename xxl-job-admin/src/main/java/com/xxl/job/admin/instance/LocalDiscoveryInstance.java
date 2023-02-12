package com.xxl.job.admin.instance;

import com.xxl.job.admin.entity.Application;
import com.xxl.job.admin.entity.Instance;
import com.xxl.job.admin.repository.ApplicationRepository;
import com.xxl.job.admin.repository.InstanceRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * @author sweeter
 * @date 2023/1/14
 */
public class LocalDiscoveryInstance implements DiscoveryInstance {

    @Resource
    private InstanceRepository instanceRepository;

    @Resource
    private ApplicationRepository applicationRepository;
    /**
     * 查询所有应用
     *
     * @return all application
     */
    @Override
    public List<Application> findAllApplications() {
        return applicationRepository.findAll();
    }

    /**
     * 查询所有实例
     *
     * @param applicationName 应用名
     * @return all instance
     */
    @Override
    public List<Instance> findAllInstances(String applicationName) {
        Specification<Instance> specification = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (StringUtils.isNotBlank(applicationName)) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("name"), applicationName));
            }
            return predicate;
        };
        return instanceRepository.findAll(specification);
    }
}
