package com.taskflow.smart_task_management.Repository;

import com.taskflow.smart_task_management.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryImplTests {


    @Autowired
    UserRepositoryImpl userRepositoryImpl;
    @Test
    public void testMethod(){
        userRepositoryImpl.getAdmins();

    }

}
