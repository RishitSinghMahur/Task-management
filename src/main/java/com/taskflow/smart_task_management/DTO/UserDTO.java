package com.taskflow.smart_task_management.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class UserDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @Schema(hidden = true)
    private ObjectId id;
    @NotEmpty
    private String  email;

}
