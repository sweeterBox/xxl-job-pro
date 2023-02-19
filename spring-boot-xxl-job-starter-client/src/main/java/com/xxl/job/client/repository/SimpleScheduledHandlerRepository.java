package com.xxl.job.client.repository;

import com.xxl.job.client.handler.AbstractHandler;
import lombok.extern.slf4j.Slf4j;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author sweeter
 * @date 2022/12/10
 */
@Slf4j
public class SimpleScheduledHandlerRepository implements ScheduledHandlerRepository {

    private static volatile ConcurrentMap<String, AbstractHandler> handlerStorage = new ConcurrentHashMap<>();

    @Override
    public AbstractHandler save(String name, AbstractHandler handler) {
        log.info("save handler storage success, name:{}, handler:{}", name, handler);
        return handlerStorage.put(name, handler);
    }

    @Override
    public AbstractHandler findOne(String name) {
        return handlerStorage.get(name);
    }

    @Override
    public Collection<AbstractHandler> findAll() {
        return handlerStorage.values();
    }

    @Override
    public void clear() {
        handlerStorage.clear();
    }
}
