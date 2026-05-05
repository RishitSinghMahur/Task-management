package com.taskflow.smart_task_management.Services;

import com.taskflow.smart_task_management.Constants.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    public <T> T get(String key,Class<T> entityClass){
       try{
           Object o = redisTemplate.opsForValue().get(key);
           ObjectMapper mapper = new ObjectMapper();
           return mapper.readValue(o.toString(),entityClass);
       }catch (Exception e){
           log.error("Error :{}",e.toString());
           return null;


       }

    }


    public void set(String key, Object o,long ttl) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error :{}", e.toString());

        }

    }

    public void set(String key, String  value, long ttl) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error :{}", e.toString());
        }
    }


    public String get(String key) {
        try {
            return redisTemplate.opsForValue().get(key).toString();
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

    public void clear(){
        List<String> keys = Arrays.asList(ApiConstants.ADVICE_API,ApiConstants.AQI_API);
        redisTemplate.delete(keys);
    }
}
