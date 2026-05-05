package com.taskflow.smart_task_management.Scheduler;

import com.taskflow.smart_task_management.DTO.TaskDTO;
import com.taskflow.smart_task_management.DTO.UserDTO;
import com.taskflow.smart_task_management.Services.EmailService;
import com.taskflow.smart_task_management.repository.UserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserScheduler {

    @Autowired
    EmailService emailService;
    @Autowired
    UserRepositoryImpl userRepositoryImpl;

    @Scheduled(cron = "0 0 12 */7 * ?")
    public void sendTaskReminders(){
        try{
            List<UserDTO> userDTOS = userRepositoryImpl.hasPendingTasks();
            for(UserDTO userDTO: userDTOS){
                List<TaskDTO> pendingTasks = userRepositoryImpl.getPendingTasks(userDTO.getId());
                String taskTitles = pendingTasks.stream().map(TaskDTO::getTitle).collect(Collectors.joining("\n"));
                emailService.sendMail(userDTO.getEmail(),"Pending task reminder","You have some tasks still pending"+taskTitles);
            }
        } catch (Exception e) {
            log.error("Error: {}", String.valueOf(e));

        }
    }

}
