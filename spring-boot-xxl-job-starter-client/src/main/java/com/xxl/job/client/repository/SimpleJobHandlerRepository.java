package com.xxl.job.client.repository;

import com.xxl.job.client.handler.AbstractJobHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author sweeter
 * @date 2022/12/10
 */
@Slf4j
public class SimpleJobHandlerRepository implements JobHandlerRepository {

    private static volatile ConcurrentMap<String, AbstractJobHandler> jobHandlerStorage = new ConcurrentHashMap<>();

    @Override
    public AbstractJobHandler save(String name, AbstractJobHandler jobHandler) {
        log.info("save jobHandler storage success, name:{}, jobHandler:{}", name, jobHandler);
        return jobHandlerStorage.put(name, jobHandler);
    }

    @Override
    public AbstractJobHandler findOne(String name) {
        return jobHandlerStorage.get(name);
    }

    @Override
    public Collection<AbstractJobHandler> findAll() {
        return jobHandlerStorage.values();
    }

    @Override
    public void clear() {
        jobHandlerStorage.clear();
    }
}
