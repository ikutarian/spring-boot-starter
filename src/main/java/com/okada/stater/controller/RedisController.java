package com.okada.stater.controller;

import com.okada.stater.pojo.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private StringRedisTemplate strRedis;

    private static final String KEY = "okada-cache";

    @RequestMapping("test")
    public JSONResult test() {
        strRedis.opsForValue().set(KEY, "hello，world~~~~");
        return JSONResult.ok(strRedis.opsForValue().get(KEY));
    }
}
