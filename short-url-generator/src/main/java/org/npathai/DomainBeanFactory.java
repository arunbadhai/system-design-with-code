package org.npathai;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import org.npathai.cache.RedirectionCache;
import org.npathai.cache.RedisRedirectionCache;
import org.npathai.client.IdGenerationServiceClient;
import org.npathai.config.MySqlDatasourceConfiguration;
import org.npathai.config.RedisConfiguration;
import org.npathai.config.UrlLifetimeConfiguration;
import org.npathai.config.ZookeeperConfiguration;
import org.npathai.controller.AnalyticsRedirectionListener;
import org.npathai.controller.RedirectionListener;
import org.npathai.dao.AnalyticsDao;
import org.npathai.dao.InMemoryAnalyticsDao;
import org.npathai.dao.MySqlRedirectionDao;
import org.npathai.dao.RedirectionDao;
import org.npathai.domain.AnalyticsService;
import org.npathai.domain.RedirectionHistoryService;
import org.npathai.domain.UrlShortener;
import org.npathai.zookeeper.DefaultZkManagerFactory;
import org.npathai.zookeeper.ZkManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.time.Clock;

@Factory
public class DomainBeanFactory {

    @Inject
    BeanContext beanContext;

    @Singleton
    public ZkManager createZkManager() throws InterruptedException {
        DefaultZkManagerFactory zkManagerFactory = new DefaultZkManagerFactory();
        return zkManagerFactory.createConnected(beanContext.getBean(ZookeeperConfiguration.class).getUrl());
    }

    @Singleton
    public RedirectionCache redirectionCache() {
        return new RedisRedirectionCache(beanContext.getBean(RedisConfiguration.class));
    }

    @Singleton
    public RedirectionDao redirectionDao() throws SQLException {
        return new MySqlRedirectionDao(beanContext.getBean(MySqlDatasourceConfiguration.class));
    }

    @Singleton
    public UrlShortener urlShortener() {
        return new UrlShortener(beanContext.getBean(UrlLifetimeConfiguration.class),
                beanContext.getBean(IdGenerationServiceClient.class),
                beanContext.getBean(RedirectionDao.class),
                beanContext.getBean(RedirectionCache.class),
                Clock.systemDefaultZone());
    }

    @Singleton
    public RedirectionHistoryService redirectionHistoryService() {
        return new RedirectionHistoryService(beanContext.getBean(RedirectionDao.class));
    }

    @Singleton
    public AnalyticsDao inMemoryAnalyticsDao() {
        return new InMemoryAnalyticsDao();
    }

    @Singleton
    public AnalyticsService analyticsService() {
        return new AnalyticsService(beanContext.getBean(UrlShortener.class),
                beanContext.getBean(AnalyticsDao.class));
    }

    @Singleton
    public RedirectionListener redirectionListener() {
        return new AnalyticsRedirectionListener(beanContext.getBean(AnalyticsService.class));
    }
}
