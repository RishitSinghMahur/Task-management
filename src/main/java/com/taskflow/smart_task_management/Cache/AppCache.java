package com.taskflow.smart_task_management.Cache;

import com.taskflow.smart_task_management.Services.RedisService;
import com.taskflow.smart_task_management.entity.TaskConfig;
import com.taskflow.smart_task_management.repository.TaskConfigRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AppCache {

    @Autowired
    TaskConfigRepository taskConfigRepository;

    @Autowired
    RedisService redisService;

    @PostConstruct
    public void init(){
        redisService.clear();
        for (TaskConfig taskConfig : taskConfigRepository.findAll()) {
            redisService.set(taskConfig.getKey(),taskConfig.getValue(),6000);
        }


    }

}
