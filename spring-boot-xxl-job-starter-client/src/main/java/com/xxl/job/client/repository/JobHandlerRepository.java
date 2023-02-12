package com.xxl.job.client.repository;

import com.xxl.job.client.handler.AbstractJobHandler;

import java.util.Collection;

/**
 * @author sweeter
 * @date 2022/12/10
 */
public interface JobHandlerRepository {

     AbstractJobHandler save(String name, AbstractJobHandler jobHandler);

     AbstractJobHandler findOne(String name);

    Collection<AbstractJobHandler> findAll();

    void clear();


}
