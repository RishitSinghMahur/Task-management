package com.taskflow.smart_task_management.repository;

import com.taskflow.smart_task_management.entity.TaskConfig;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskConfigRepository extends MongoRepository<TaskConfig, ObjectId> {

}
