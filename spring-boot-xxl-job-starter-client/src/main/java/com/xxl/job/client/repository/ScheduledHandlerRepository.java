package com.xxl.job.client.repository;

import com.xxl.job.client.handler.AbstractHandler;

import java.util.Collection;

/**
 * @author sweeter
 * @date 2022/12/10
 */
public interface ScheduledHandlerRepository {

     AbstractHandler save(String name, AbstractHandler handler);

     AbstractHandler findOne(String name);

    Collection<AbstractHandler> findAll();

    void clear();


}
