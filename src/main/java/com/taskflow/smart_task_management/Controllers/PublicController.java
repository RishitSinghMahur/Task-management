package com.taskflow.smart_task_management.Controllers;

import com.taskflow.smart_task_management.DTO.UserDTO;
import com.taskflow.smart_task_management.DTO.UserRequestDTO;
import com.taskflow.smart_task_management.Services.TaskService;
import com.taskflow.smart_task_management.Services.UserService;
import com.taskflow.smart_task_management.Services.UserServiceDetailsImpl;
import com.taskflow.smart_task_management.entity.User;
import com.taskflow.smart_task_management.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/public")
@Tag(name = "1. Login / Signup")
public class PublicController {

    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;
    @Autowired
    AuthenticationManager  authenticationManager;
    @Autowired
    UserServiceDetailsImpl userServiceDetailsImpl;
    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/signup")
    @Operation(summary = "2. Not an existing user")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        Optional<User> targetUser = Optional.ofNullable(userService.findUserByUsername(user.getUsername()));
        try{
            if(targetUser.isEmpty()){
                userService.saveNewUser(user);
                return new ResponseEntity<>(user,HttpStatus.CREATED);

            }
            else{
                System.out.println("User already exists");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }catch (Exception e){
            System.out.println("there was a problem "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }




    }


    @PostMapping("/login")
    @Operation(summary = "1. Already have an account")
    public ResponseEntity<String> login(@RequestBody UserRequestDTO userRequestDTO){
        try{
            User user = new User();
            user.setUsername(userRequestDTO.getUsername());
            user.setPassword(userRequestDTO.getPassword());
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));//internally calls userDetailsImpl and also passEncder bean to check passwoed
            UserDetails userDetails = userServiceDetailsImpl.loadUserByUsername(user.getUsername());
            String token = jwtUtils.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(token,HttpStatus.OK);
        }catch (Exception e){
            log.error("Error :{}",e.toString());
            return new ResponseEntity<>("Incorrect username or password",HttpStatus.BAD_REQUEST);
        }

    }


}
