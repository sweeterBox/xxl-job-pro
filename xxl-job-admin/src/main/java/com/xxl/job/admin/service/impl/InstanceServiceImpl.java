package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.common.jpa.query.QueryHandler;
import com.xxl.job.admin.entity.Instance;
import com.xxl.job.admin.query.InstanceQuery;
import com.xxl.job.admin.repository.InstanceRepository;
import com.xxl.job.admin.service.InstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * @author sweeter
 * @date 2022/9/3
 */
@Transactional(rollbackFor = Exception.class,readOnly = true)
@Slf4j
@Service
public class InstanceServiceImpl implements InstanceService {

    @Autowired
    private InstanceRepository instanceRepository;


    /**
     * 查询所有的应用实例
     *
     * @return List<Instance>
     */
    @Override
    public List<Instance> findAll(String applicationName) {
        Specification<Instance> specification = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (StringUtils.isNotBlank(applicationName)) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("name"), applicationName));
            }
            return predicate;
        };
        return this.instanceRepository.findAll(specification);
    }

    @Override
    public ResultPage<Instance> findPageList(InstanceQuery query) {
        Page<Instance> page = this.instanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHandler.getPredicate(root, query, criteriaBuilder), query.createPageRequest());
        return ResultPage.of(page);
    }

}
