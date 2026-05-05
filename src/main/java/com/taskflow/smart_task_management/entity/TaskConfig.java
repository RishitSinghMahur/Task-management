package com.taskflow.smart_task_management.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("configuration_task_management")
@Data
public class TaskConfig {

    private String key;
    private String value;
}
