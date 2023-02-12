package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.common.jpa.query.QueryHandler;
import com.xxl.job.admin.entity.Application;
import com.xxl.job.admin.entity.Instance;
import com.xxl.job.admin.enums.InstanceStatus;
import com.xxl.job.admin.model.ApplicationInfo;
import com.xxl.job.admin.query.ApplicationQuery;
import com.xxl.job.admin.repository.ApplicationRepository;
import com.xxl.job.admin.repository.InstanceRepository;
import com.xxl.job.admin.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author sweeter
 * @description
 * @date 2022/9/3
 */
@Transactional(rollbackFor = Exception.class,readOnly = true)
@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Resource
    private ApplicationRepository applicationRepository;

    @Resource
    private InstanceRepository instanceRepository;

    @Override
    public ResultPage<ApplicationInfo> findPageList(ApplicationQuery query) {
        Page<Application> page = applicationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHandler.getPredicate(root, query, criteriaBuilder), query.createPageRequest());
        //查询实例信息
        List<String> names = page.get().map(Application::getName).collect(Collectors.toList());
        List<Instance> instances = instanceRepository.findAllByNameIn(names);
        Map<String, List<Instance>> instanceMaps = instances.stream().collect(Collectors.groupingBy(Instance::getName));

        Page<ApplicationInfo> infoPage = page.map(new ApplicationInfo())
                .map(v -> {
                    List<Instance> instanceList = instanceMaps.get(v.getName());
                    int allSize = Optional.ofNullable(instanceList).map(List::size).orElse(0);
                    int healthySize = Optional.ofNullable(instanceList).map(List::stream).orElse(Stream.empty()).filter(Instance::getHealthy).filter(i -> i.getStatus().equals(InstanceStatus.UP)).collect(Collectors.toList()).size();
                    List<String> clientUrls = Optional.ofNullable(instanceList).map(List::stream).orElse(Stream.empty()).map(Instance::getUrl).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
                    v.setInstanceAllSize(allSize);
                    v.setInstanceHealthySize(healthySize);
                    v.setInstanceUrls(clientUrls);
                    return v;
                });
        return ResultPage.of(infoPage);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        this.applicationRepository.deleteById(id);
    }

    @Override
    public ApplicationInfo findOne(Long id) {
        Application application = new Application();
        application.setId(id);
        Example<Application> example = Example.of(application);
        ApplicationInfo applicationInfo = applicationRepository.findOne(example).map(new ApplicationInfo()).orElseThrow(() -> new RuntimeException("记录不存在"));
        //查询应用实例
        List<Instance> instances = instanceRepository.findByNameAndStatus(applicationInfo.getName(), InstanceStatus.UP);
        if (!CollectionUtils.isEmpty(instances)) {
            List<String> clientUrls = instances.stream().map(Instance::getUrl).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
            int allSize = instances.size();
            int healthySize = instances.stream().filter(Instance::getHealthy).collect(Collectors.toList()).size();
            applicationInfo.setInstanceAllSize(allSize);
            applicationInfo.setInstanceHealthySize(healthySize);
            applicationInfo.setInstanceUrls(clientUrls);
        }
        return applicationInfo;
    }

    @Override
    public ApplicationInfo findOne(String name) {
        Application application = new Application();
        application.setName(name);
        Example<Application> example = Example.of(application);
        ApplicationInfo applicationInfo = applicationRepository.findOne(example).map(new ApplicationInfo()).orElseThrow(() -> new RuntimeException("记录不存在"));
        //查询应用实例
        List<Instance> instances = instanceRepository.findByNameAndStatus(applicationInfo.getName(), InstanceStatus.UP);
        if (!CollectionUtils.isEmpty(instances)) {
            List<String> clientUrls = instances.stream().map(Instance::getUrl).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
            int allSize = instances.size();
            int healthySize = instances.stream().filter(Instance::getHealthy).collect(Collectors.toList()).size();
            applicationInfo.setInstanceAllSize(allSize);
            applicationInfo.setInstanceHealthySize(healthySize);
            applicationInfo.setInstanceUrls(clientUrls);
        }
        return applicationInfo;
    }

    @Transactional
    @Override
    public void save(Application application) {
        this.applicationRepository.save(application);
    }

    @Override
    public List<Application> findAll() {

        return applicationRepository.findAll();
    }

}
