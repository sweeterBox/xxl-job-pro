package com.xxl.job.cloud.listener;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.utils.NamingUtils;
import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

//import lombok.extern.slf4j.Slf4j;

/**
 * @author sweeter
 * @description
 * @date 2022/12/26
 */
//@Slf4j
public class InstancesChangeEventListener extends Subscriber<InstancesChangeEvent> implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Autowired
    private CompositeInstanceUpAndDownListener instanceUpAndDownListenerDelegate;

    /**
     * 当前实例
     */
    private Instance currInstance;

    private NamingService namingService() {
        return this.nacosServiceManager.getNamingService(this.nacosDiscoveryProperties.getNacosProperties());
    }

    @PostConstruct
    private void post(){
        NotifyCenter.registerSubscriber(this);
    }

    /**
     * Event callback.
     *
     * @param event {@link Event}
     */
    @Override
    public void onEvent(InstancesChangeEvent event) {
        try {
            //log.debug("接收到 InstancesChangeEvent 订阅事件：{}", GsonTool.toJson(event));
            NamingService namingService = this.namingService();
            List<Instance> instances;
            try {
                instances = namingService.getAllInstances(nacosDiscoveryProperties.getService(), nacosDiscoveryProperties.getGroup());
            } catch (Exception e) {
               // log.error(e.getMessage());
                instances = event.getHosts();
            }

            Optional<Instance> instanceOpt = Optional.ofNullable(instances).map(Collection::stream).orElse(Stream.empty())
                    .filter(instance -> instance.getServiceName().equals(NamingUtils.getGroupedName(nacosDiscoveryProperties.getService(), nacosDiscoveryProperties.getGroup()))
                            && instance.getIp().equalsIgnoreCase(nacosDiscoveryProperties.getIp())
                            && instance.getPort() == nacosDiscoveryProperties.getPort()
                    ).findAny();
            if (instanceOpt.isPresent()) {
                Instance instance = instanceOpt.get();
                Instance oldCurrInstance = this.currInstance;
                this.currInstance = instance;
                if (Objects.nonNull(oldCurrInstance) && !oldCurrInstance.isEnabled()) {
                    //上线
                    //currInstance.setEnabled(true);
                    this.instanceUpAndDownListenerDelegate.up(this.currInstance, instances);
                }
                if (Objects.isNull(oldCurrInstance)) {
                    //第一次上线
                    this.instanceUpAndDownListenerDelegate.up(this.currInstance, instances);
                }
            }else {
                //当前实例不在实例列表中
                if (Objects.nonNull(this.currInstance) && this.currInstance.isEnabled()) {
                    //下线
                    this.currInstance.setEnabled(false);
                    this.instanceUpAndDownListenerDelegate.down(this.currInstance, instances);

                }

                if (Objects.isNull(this.currInstance)){
                    //初始化启动时服务就处于下线状态
                    this.currInstance = this.getNewInstance();
                    this.currInstance.setEnabled(false);
                    this.instanceUpAndDownListenerDelegate.down(this.currInstance, instances);
                }

            }
        } catch (Throwable e) {
            //log.error("InstancesChangeEventListener exception:{}",e.getMessage());
        }
    }

    public Instance getCurrInstance() {
        return Optional.ofNullable(this.currInstance)
                .orElseGet(this::getNewInstance);

    }

    private Instance getNewInstance() {
        Instance instance = new Instance();
        String groupedServiceName = NamingUtils.getGroupedName(nacosDiscoveryProperties.getService(), nacosDiscoveryProperties.getGroup());
        instance.setServiceName(groupedServiceName);
        instance.setIp(nacosDiscoveryProperties.getIp());
        instance.setPort(nacosDiscoveryProperties.getPort());
        instance.setWeight(this.nacosDiscoveryProperties.getWeight());
        instance.setClusterName(this.nacosDiscoveryProperties.getClusterName());
        instance.setEnabled(this.nacosDiscoveryProperties.isInstanceEnabled());
        instance.setMetadata(nacosDiscoveryProperties.getMetadata());
        instance.setEphemeral(this.nacosDiscoveryProperties.isEphemeral());
        return instance;
    }

    /**
     * Type of this subscriber's subscription.
     *
     * @return Class which extends {@link Event}
     */
    @Override
    public Class<? extends com.alibaba.nacos.common.notify.Event> subscribeType() {
        return InstancesChangeEvent.class;
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}

