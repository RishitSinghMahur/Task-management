package com.taskflow.smart_task_management.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Document(collection = "Tasks")
@CompoundIndex(name = "user_task_unique",def = "{'userId':1,'title':1}",unique = true)
@Data
@NoArgsConstructor
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "status",
        "userId",
        "createdAt"
})
public class Task {
    @Id
    private String id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    private String status;
    @NonNull
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId userId;
    private LocalDateTime createdAt;


}
