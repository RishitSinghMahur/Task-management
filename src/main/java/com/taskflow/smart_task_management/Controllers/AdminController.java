package com.taskflow.smart_task_management.Controllers;


import com.taskflow.smart_task_management.Cache.AppCache;
import com.taskflow.smart_task_management.Services.EmailService;
import com.taskflow.smart_task_management.Services.TaskService;
import com.taskflow.smart_task_management.Services.UserService;
import com.taskflow.smart_task_management.entity.Task;
import com.taskflow.smart_task_management.entity.TaskConfig;
import com.taskflow.smart_task_management.entity.User;
import io.micrometer.observation.annotation.ObservationKeyValue;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@Slf4j
@RestController
@RequestMapping("/admin")
@Tag(name = "4. Admin tools")
public class AdminController {

    @Autowired
    AppCache appCache;

    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;
    @Autowired
    EmailService emailService;

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/create-admin/id/{myId}")
    public ResponseEntity<?> createAdmin(@PathVariable String myId) {
        try {
            Optional<User> userById = userService.findUserById(new ObjectId(myId));
            if (userById.isEmpty()) {
                return new ResponseEntity<>("User not found corresponding to this id", HttpStatus.NOT_FOUND);
            }
            User user = userById.get();

            if (user.getRoles().contains("ADMIN")) {
                return new ResponseEntity<>("Already an admin", HttpStatus.BAD_REQUEST);
            }

            user.getRoles().add("ADMIN");

            userService.saveUser(user);
            emailService.sendMail(user.getEmail(),"New authorization","You have been given admin controls");
            return new ResponseEntity<>("Admin created :" + user.getUsername() + "\nUserId :" + user.getId(), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error :{}", e.toString());
            return new ResponseEntity<>("Could not be created admin check logs",HttpStatus.BAD_REQUEST);
        }
    }


    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }


    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get-user/id/{myId}")
    public ResponseEntity<?> getUserById(@PathVariable String myId){
        Optional<User> userById = userService.findUserById(new ObjectId(myId));
        if(userById.isEmpty()){
            return new ResponseEntity<>("User does not exists corresponding to this id",HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(userById.get(),HttpStatus.OK);
        }

    }


    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get-task/id/{myId}")
    public ResponseEntity<?> getTaskById(@PathVariable String myId){
        Optional<Task> taskById = taskService.findTaskById(new ObjectId(myId));
        if(taskById.isEmpty()){
            return new ResponseEntity<>("Task does not exists corresponding to this id",HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(taskById.get(),HttpStatus.OK);
        }

    }


    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-user/id/{myId}")
    public ResponseEntity<?> deleteUserById(@PathVariable String myId){
        Optional<User> userById = userService.findUserById(new ObjectId(myId));
        if(userById.isEmpty()){
            return new ResponseEntity<>("User does not exists corresponding to this id",HttpStatus.NOT_FOUND);
        }else{
            userService.deleteUser(userById.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }


    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-task/id/{myId}")
    public ResponseEntity<?> deleteTaskById(@PathVariable String myId){
        Optional<Task> taskById = taskService.findTaskById(new ObjectId(myId));
        if(taskById.isEmpty()){
            return new ResponseEntity<>("Task does not exists corresponding to this id",HttpStatus.NOT_FOUND);
        }else{
            taskService.deleteTask(taskById.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }



    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/clear-cache")
    public ResponseEntity<?> init(){
        appCache.init();
        return new ResponseEntity<>("Cache cleared",HttpStatus.OK);

    }









}
