package com.taskflow.smart_task_management.repository;

import com.taskflow.smart_task_management.entity.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, ObjectId> {

}
