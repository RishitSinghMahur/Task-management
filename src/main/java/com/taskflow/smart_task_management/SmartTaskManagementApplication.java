package com.taskflow.smart_task_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
@SpringBootApplication
public class SmartTaskManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartTaskManagementApplication.class, args);
	}



	@Bean
	public PlatformTransactionManager manager(MongoDatabaseFactory dbFactory){
		return new MongoTransactionManager(dbFactory);
	}

}
