package com.bite.common.utils;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class JsonUtil<T> {

    public static <T> T parseJson(String json, Class<T> clazz) {
        if(!StringUtils.hasLength(json) || clazz == null){
            return null;
        }
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.error("JsonUtil parseJson error, e:", e);
            return  null;
        }
    }

    public static String toJson(Object object) {
        try {
            return object == null ? null : JSON.toJSONString(object);
        } catch (Exception e) {
            log.error("JsonUtil toJson error, e:", e);
            return null;
        }
    }
}
