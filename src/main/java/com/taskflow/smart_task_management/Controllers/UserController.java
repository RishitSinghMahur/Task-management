package com.taskflow.smart_task_management.Controllers;


import com.taskflow.smart_task_management.DTO.UserDTO;
import com.taskflow.smart_task_management.Services.AQIService;
import com.taskflow.smart_task_management.Services.AdivceService;
import com.taskflow.smart_task_management.Services.TaskService;
import com.taskflow.smart_task_management.Services.UserService;
import com.taskflow.smart_task_management.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "3. User Settings", description = "Get details\nUpdate credentials\nDelete account")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;
    @Autowired
    AdivceService adivceService;
    @Autowired
    AQIService aqiService;


    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/1-greetings")
    @Operation(summary = "1. Just wanna say hi!")
    public ResponseEntity<?> greetings(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);
        return new ResponseEntity<>("Hi! "+user.getUsername()+
                "\nYour city's AQI: "+aqiService.getAQI("Delhi").getAqi()+
                "\n"+adivceService.getAdviceResponse().getAdvice(),HttpStatus.OK);

    }
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/2-get-user-details")
    @Operation(summary = "2. Get user details")
    public ResponseEntity<?> getUserDetails(){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findUserByUsername(username);
            return new ResponseEntity<>(user,HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("An error occured"+e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        }


    }


    @Transactional
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/3-update-details")
    @Operation(summary = "3. Update user details")
    public ResponseEntity<?> updateUserDetails(@RequestBody UserDTO newUser){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User oldUser = userService.findUserByUsername(username);
        try{
            oldUser.setUsername(newUser.getUsername()!= null  && !newUser.getUsername().isEmpty()? newUser.getUsername() : oldUser.getUsername());
            oldUser.setPassword(newUser.getPassword()!= null && !newUser.getPassword().isEmpty()  ? newUser.getPassword() : oldUser.getPassword());
            oldUser.setEmail(newUser.getEmail()!= null && !newUser.getEmail().isEmpty() ? newUser.getEmail() : oldUser.getEmail());
            userService.saveUser(oldUser);
            return new ResponseEntity<>(oldUser, HttpStatus.OK);
        }catch (Exception e){
            System.out.printf("An error occured "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST );
        }

    }


    @Transactional
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/4-delete-user")
    @Operation(summary = "4. Delete account")
    public ResponseEntity<?> deleteUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);
        try{
            userService.deleteUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (Exception e){
            System.out.printf("An error occured "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST );
        }

    }




}
