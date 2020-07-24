package com.jakob.springbootdemo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.val;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManagerWithCaffeine() {
        val manager = new CaffeineCacheManager();
        val cacheBuilder = Caffeine.newBuilder()
                .maximumSize(10000);
//            .expireAfterAccess(1, TimeUnit.HOURS)
//            .expireAfterWrite(3, TimeUnit.MINUTES)
        manager.setCaffeine(cacheBuilder);
        return manager;
    }
}
