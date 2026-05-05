package com.taskflow.smart_task_management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Document(collection = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    @NonNull
    @Indexed(unique = true)
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String  email;
    private List<String> roles;
    @DBRef
    private List<Task> taskList = new ArrayList<>();



    }


