package com.taskflow.smart_task_management.repository;

import com.taskflow.smart_task_management.DTO.TaskDTO;
import com.taskflow.smart_task_management.DTO.UserDTO;
import com.taskflow.smart_task_management.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class UserRepositoryImpl {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    Query query;

    public List<com.taskflow.smart_task_management.entity.User> getAdmins(){
        query.addCriteria(Criteria.where("roles").is("ADMIN"));
        List<com.taskflow.smart_task_management.entity.User> users = mongoTemplate.find(query, User.class);
        return users;
    }

    public List<UserDTO> hasPendingTasks(){
        query.addCriteria(Criteria.where("taskList.status").is("PENDING"));
        List<UserDTO> userInfo = mongoTemplate.find(query, UserDTO.class);
        return userInfo;

    }



    public List<TaskDTO> getPendingTasks(ObjectId userId){
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        query.addCriteria(Criteria.where("userId").is(userId)
                .and("status").is("PENDING")
                .and("createdAt").lte(sevenDaysAgo));
        query.fields().include("title");
        List<TaskDTO> taskInfo = mongoTemplate.find(query, TaskDTO.class);
        return taskInfo;

    }
}
