package com.xxl.job.admin.lock;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * @author sweeter
 * @date 2023/1/15
 */
public interface LockRepository extends JpaRepository<LockEntity, String> {

    //@Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Override
    Optional<LockEntity> findById(String lockKey);

}
