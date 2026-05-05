package com.taskflow.smart_task_management.Services;

import com.taskflow.smart_task_management.entity.Task;
import com.taskflow.smart_task_management.entity.User;
import com.taskflow.smart_task_management.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserService userService;

    @Transactional
    public void saveNewTask(String username, Task task) {
        try{

            User user = userService.findUserByUsername(username);
            task.setCreatedAt(LocalDateTime.now());
            task.setStatus("PENDING");
            task.setUserId(new ObjectId(user.getId()));
            taskRepository.save(task);
            user.getTaskList().add(task);
            userService.saveUser(user);


        }catch (Exception e){
            System.out.println("there was an error saving task"+e.getMessage());
            e.printStackTrace();
        }


    }


    public void saveTask(Task task){
        taskRepository.save(task);
    }

    @Transactional
    public void deleteTaskById(ObjectId id){
        try{
            taskRepository.deleteById(id);
        }catch (Exception e){
            System.out.println("An error occured "+e.getMessage());
        }
    }


    public Optional<Task> findTaskById(ObjectId id){
        return taskRepository.findById(id);
    }

    public void deleteTask(Task task){
        taskRepository.delete(task);
    }




}
