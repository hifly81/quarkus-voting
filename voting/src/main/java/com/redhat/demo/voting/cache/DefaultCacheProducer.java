package com.redhat.demo.voting.cache;

import com.redhat.demo.voting.model.Poll;
import io.quarkus.arc.AlternativePriority;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.concurrent.TimeUnit;


@ApplicationScoped
public class DefaultCacheProducer {

    public static final Logger log = Logger.getLogger(DefaultCacheProducer.class);

    @ConfigProperty(name = "poll.cache.mode", defaultValue = "DIST_ASYNC")
    String cacheModeValue;

    @ConfigProperty(name = "poll.cache.entry.lifespan.hours", defaultValue = "1")
    String cacheEntryLifespan;

    @Produces
    @DefaultCache
    public Cache<Long, Poll> returnCache() {
        Cache<Long, Poll> cache = defaultCacheContainer().getCache();
        return cache;
    }

    @Produces
    public Configuration defaultCacheConfiguration() {

        log.info("Configuring default-cache...");

        CacheMode cacheMode = CacheMode.valueOf(cacheModeValue);

        Configuration config = new ConfigurationBuilder()
                .clustering()
                .cacheMode(cacheMode)
                .l1().lifespan(25000L)
                .hash().numOwners(2)
                .expiration()
                .lifespan(Long.valueOf(cacheEntryLifespan), TimeUnit.HOURS)
                .build();

        return config;
    }

    @Produces
    @AlternativePriority(1)
    public EmbeddedCacheManager defaultCacheContainer() {

        GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
                .clusteredDefault()
                .defaultCacheName("default")
                .serialization()
                .addContextInitializers(new PollsSerializationContextInitializerImpl())
                .build();

        return new DefaultCacheManager(globalConfig, defaultCacheConfiguration());
    }

}
