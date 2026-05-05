package com.taskflow.smart_task_management.repository;

import com.taskflow.smart_task_management.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    public User findByUsername(String username);

}
