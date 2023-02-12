package com.xxl.job.admin.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * @author sweeter
 * @date 2023/1/15
 */
@Slf4j
@Service
public class DbLock implements Lock {

    /**
     * Propagation.NOT_SUPPORTED:以非事务的方式运行，如果当前存在事务，则挂起当前事务
     */

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

    @Autowired
    private LockRepository lockRepository;

    /**
     * 获取锁
     *
     * @param key lock key
     * @return boolean
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public boolean lock(String key) {
        return this.lock(key, DEFAULT_TIME_OUT, DEFAULT_EXPIRE_TIME);
    }

    /**
     * 获取锁
     *
     * @param key    lock key
     * @param expire lock 超时时间 单位/毫秒 注：该时间应该大于业务处理的时间
     * @return boolean
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public boolean lock(String key, Long expire) {
        return this.lock(key, DEFAULT_TIME_OUT, expire);
    }

    /**
     * 获取锁
     *
     * @param key     lock key
     * @param timeOut 取lock 超时时间 单位/毫秒
     * @param expire  lock 超时时间 单位/毫秒 注：该时间应该大于业务处理的时间
     * @return boolean
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public boolean lock(String key, Long timeOut, Long expire) {
        expire = Math.max(expire, DEFAULT_TIME_OUT);
        long reqLockEndTime = System.currentTimeMillis() + Math.max(timeOut,DEFAULT_TIME_OUT);
        while (System.currentTimeMillis() < reqLockEndTime) {
            if (this.tryLock(key, timeOut, expire)) {
                log.info("{}，获取到锁", key);
                return Boolean.TRUE;
            }
            try {
                log.info("key[{}]空转取锁", key);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean tryLock(String key, Long timeOut, Long expire) {
        boolean success = false;
        try {
            TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
            Optional<LockEntity> dbLockOpt = lockRepository.findById(key);
            if (dbLockOpt.isPresent()) {
                LockEntity en = dbLockOpt.get();
                if (System.currentTimeMillis() - en.getCreateTimestamp() > en.getExpireTime()) {
                    en.setExpireTime(expire);
                    en.setTimeOut(timeOut);
                    en.setCreateTimestamp(System.currentTimeMillis());
                    lockRepository.save(en);
                    success = true;
                }
            }else {
                LockEntity en = new LockEntity(key, expire, timeOut, System.currentTimeMillis());
                lockRepository.save(en);
                success = true;
            }
            platformTransactionManager.commit(transaction);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return success;
    }

    /**
     * 释放锁
     *
     * @param key lock key
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public boolean unlock(String key) {
        try {
            if (lockRepository.existsById(key)) {
                lockRepository.deleteById(key);
                log.info("{},解锁", key);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
