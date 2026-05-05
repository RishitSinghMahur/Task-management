package com.taskflow.smart_task_management.Service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
@SpringBootTest
public class RedisTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Disabled
    @Test
    public void testing(){
        redisTemplate.opsForValue().set("email","user@gmail.com");
        Object email = redisTemplate.opsForValue().get("email");
        int b=0;


    }}

