package com.taskflow.smart_task_management.Controllers;

import com.taskflow.smart_task_management.DTO.TaskDTO;
import com.taskflow.smart_task_management.Services.TaskService;
import com.taskflow.smart_task_management.Services.UserService;
import com.taskflow.smart_task_management.Services.UserServiceDetailsImpl;
import com.taskflow.smart_task_management.entity.Task;
import com.taskflow.smart_task_management.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/task")
@Tag(name = "2. Manage Your Tasks here")

public class TaskController {

    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get")
    @Operation(summary = "2. Show all user tasks")
    public ResponseEntity<?> getAllUserTask(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);
        if(user.getTaskList().isEmpty() && user.getTaskList()!=null){
            return new ResponseEntity<>("No tasks found",HttpStatus.NOT_FOUND);
        }
        List<Task> taskList = user.getTaskList();
        return new ResponseEntity<>(taskList,HttpStatus.OK);

    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get/id/{myId}")
    @Operation(summary = "3. Find task by task-id")
    public ResponseEntity<?> getTaskById(@PathVariable String myId){
        try{
            ObjectId objectId = new ObjectId((myId));
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findUserByUsername(username);
            Optional<Task> taskById = taskService.findTaskById(objectId);
            if(user.getTaskList().isEmpty() && user.getTaskList()!=null){
                return new ResponseEntity<>("User has no Tasks",HttpStatus.NOT_FOUND);
            }
            if(!taskById.isPresent()){
                return new ResponseEntity<>("No task Found",HttpStatus.NOT_FOUND);

            }
            Task task = taskById.get();
            if(!user.getId().equals(task.getUserId().toString())){
                return new ResponseEntity<>("Unauthorized",HttpStatus.FORBIDDEN);

            }
            return new ResponseEntity<>(task,HttpStatus.OK);
        }catch(Exception e){
            log.error(e.toString());
            return null;
        }

    }



    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/create-task")
    @Operation(summary = "1. Create new task")
    public ResponseEntity<?> createNewTask(@RequestBody TaskDTO taskDTO){
        try{
            Task task = new Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            taskService.saveNewTask(username,task);
            return new ResponseEntity<>(task, HttpStatus.CREATED);
        }catch (Exception e){
            System.out.println("encountered an error -> "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/id/{myId}")
    @Operation(summary = "5. Delete task")
    public ResponseEntity<?> deleteTask(@PathVariable String  myId){
        try{
            ObjectId objectId = new ObjectId((myId));
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findUserByUsername(username);
            Optional<Task> taskById = taskService.findTaskById(objectId);
            if(!taskById.isPresent()){
                return new ResponseEntity<>("Task not found",HttpStatus.NOT_FOUND);
            }

            Task task = taskById.get();

            if(!user.getId().equals(task.getUserId().toString())){
                return new ResponseEntity<>("UNAUTHORIZED",HttpStatus.FORBIDDEN );
            }

            taskService.deleteTaskById(objectId);
            return new ResponseEntity<>(task,HttpStatus.OK);
        }catch (Exception e){
            System.out.println("encountered an error -> "+e.getMessage());

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("id/{myId}")
    @Operation(summary = "4. Update task")
    public ResponseEntity<?> updateById(@PathVariable String myId,@RequestBody Task newTask){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);
        List<Task> list = user.getTaskList().stream().filter(x -> x.getId().equals(myId)).toList();
        if(!list.isEmpty()){
            Task oldtask = list.get(0);
            oldtask.setTitle(newTask.getTitle()!=null && !newTask.getTitle().isEmpty()? newTask.getTitle():oldtask.getTitle());
            oldtask.setDescription(newTask.getDescription()!=null && !newTask.getDescription().isEmpty()? newTask.getDescription():oldtask.getDescription());
            oldtask.setStatus(newTask.getStatus()!=null && !newTask.getStatus().isEmpty()? newTask.getStatus():oldtask.getStatus());
            taskService.saveTask(oldtask);
            return new ResponseEntity<>(newTask,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }

}
