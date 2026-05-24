package com.bite.common.config;

import com.bite.common.utils.Redis;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
    @Bean
    @ConditionalOnProperty(prefix = "spring.data.redis", name = "host")
    public Redis redis(StringRedisTemplate stringRedisTemplate) {
        return new Redis(stringRedisTemplate);
    }
}
