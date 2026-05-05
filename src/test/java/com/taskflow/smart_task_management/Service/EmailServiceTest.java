package com.taskflow.smart_task_management.Service;

import com.taskflow.smart_task_management.Services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    public void sendMailTest(){
        emailService.sendMail("user112@mailservice.com","Discord","Valo aaja");
        emailService.sendMail("rishitmahur69@gmail.com","Discord","Valo aaja");
    }
}
