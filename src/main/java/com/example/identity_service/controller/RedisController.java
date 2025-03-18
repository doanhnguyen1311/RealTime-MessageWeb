package com.example.identity_service.controller;

import com.example.identity_service.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/set")
    public String setKey(@RequestBody Map<String, Object> request) {
        String key = (String) request.get("key");
        String value = (String) request.get("value");
        long ttl = request.containsKey("ttl") ? Long.parseLong(request.get("ttl").toString()) : 0;
        if (key == null || value == null) {
            return "Error: Key and value are required";
        }
        redisService.set(key, value, ttl);
        return "Set key " + key + " with value " + value;
    }

    @GetMapping("/get/{key}")
    public String getKey(@PathVariable String key) {
        String value = redisService.get(key);
        return value != null ? "Value: " + value : "Key not found";
    }
}
