package com.xxl.job.admin.repository;

import com.xxl.job.admin.common.jpa.repository.BaseJpaRepository;
import com.xxl.job.admin.entity.Instance;
import com.xxl.job.admin.enums.InstanceStatus;
import java.util.List;
import java.util.Optional;

/**
 * @author sweeter
 * @date 2022/9/3
 */
public interface InstanceRepository extends BaseJpaRepository<Instance, Long> {

    Optional<Instance> findAllByNameAndUrl(String name, String url);

    List<Instance> findByNameAndStatus(String name, InstanceStatus status);

    List<Instance> findAllByNameIn(List<String> names);

    Long countAllByStatus(InstanceStatus status);

}
