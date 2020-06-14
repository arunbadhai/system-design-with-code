package org.npathai;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Factory;
import org.npathai.config.ZookeeperConfiguration;
import org.npathai.domain.IdGenerationService;
import org.npathai.zookeeper.DefaultZkManagerFactory;
import org.npathai.zookeeper.ZkManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Factory
public class ObjectMother {

    @Inject
    BeanContext beanContext;

    @Singleton
     public ZkManager createZkManager() throws InterruptedException {
        DefaultZkManagerFactory zkManagerFactory = new DefaultZkManagerFactory();
        return zkManagerFactory.createConnected(
                beanContext.getBean(ZookeeperConfiguration.class).getUrl());
    }

    @Singleton
    public IdGenerationService idGenerationService() throws Exception {
        return new IdGenerationService(beanContext.getBean(ZkManager.class),
                beanContext.getBean(ScheduledJobService.class));
    }
}
