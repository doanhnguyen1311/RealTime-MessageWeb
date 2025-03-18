package com.example.identity_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    // luu key, value

    public void set(String key, String value, long ttl){
        redisTemplate.opsForValue().set(key, value);

        if(ttl > 0){
            redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
        }
    }

    // lay value theo key
    public String get(String key){
        Object value = redisTemplate.opsForValue().get(key);
        return value == null ? "" : value.toString();
    }

    // xoa key
    public void delete (String key){
        redisTemplate.delete(key);
    }
}
