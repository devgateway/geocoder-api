package org.devgateway.geocoder.spring;

import net.sf.ehcache.management.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import javax.management.MBeanServer;

/**
 * @author idobre
 * @since 14/12/2017
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    @Autowired(required = false)
    private MBeanServer mbeanServer;

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        ehCacheManagerFactoryBean.setShared(true);
        return ehCacheManagerFactoryBean;
    }

    @Bean
    public CacheManager cacheManager(final EhCacheManagerFactoryBean factory) {
        return new EhCacheCacheManager(factory.getObject());
    }

    @Bean(destroyMethod = "dispose", initMethod = "init")
    @Profile("!integration")
    public ManagementService ehCacheManagementService(final EhCacheManagerFactoryBean factory) {
        ManagementService managementService =
                new ManagementService(factory.getObject(), mbeanServer, true, true, true, true);
        return managementService;
    }

}
