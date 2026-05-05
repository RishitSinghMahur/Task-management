package com.taskflow.smart_task_management.Services;

import com.taskflow.smart_task_management.entity.Task;
import com.taskflow.smart_task_management.entity.User;
import com.taskflow.smart_task_management.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<Task> getUserTasks(String username){
        User user = userRepository.findByUsername(username);
        return user.getTaskList();


    }
//
    public void saveNewUser(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            emailService.sendMail(
                    user.getEmail(),
                    "New user",
                    "Hi "+user.getUsername()+" thanks for signing up in Task Forge");
        }catch (Exception e){
            log.error("Error :{}",e.toString());
        }


    }

    public Optional<User> findUserById(ObjectId id){
        return userRepository.findById(id);

    }


    public void saveUser(User user){
        userRepository.save(user);
    }

    public User findUserByUsername(String username){
        return userRepository.findByUsername(username);

    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

}
