package com.bite.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
public class Redis {

    private static final String REDIS_SPLIT = ":";
    private static final String REDIS_DEFAULT_PREFIX = "default";

    private StringRedisTemplate stringRedisTemplate;

    public Redis(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String get(String key) {
        try {
            return key == null ? null : stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("redis get error, key:{}", key);
            return null;
        }
    }

    public boolean hasKey(String key) {
        try {
            return  key == null ? false : stringRedisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("redis hasKey error, key:{}", key);
            return false;
        }
    }

    public boolean set(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis set error, key:{}, value:{}, e:{}", key, value, e);
            return false;
        }
    }


    public boolean set(String key, String value, long timeout) {
        try {
            if(timeout > 0){
                stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("redis set error, key:{}, value:{}", key, value);
            return false;
        }
    }

    public String buildKey(String prefix, String... args) {
        if (prefix == null) {
            prefix = REDIS_DEFAULT_PREFIX;
        }
        StringBuilder key = new StringBuilder();
        key.append(prefix);
        if (args != null) {
            for(String arg : args) {
                key.append(REDIS_SPLIT).append(arg);
            }
        }
        return key.toString();
    }
}
