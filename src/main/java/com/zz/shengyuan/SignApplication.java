package com.zz.shengyuan;

import com.zz.shengyuan.storage.StorageProperties;
import com.zz.shengyuan.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;



@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(StorageProperties.class)
public class SignApplication {

	@Bean
	public TaskScheduler taskScheduler(){
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(10);
		taskScheduler.setThreadNamePrefix("sb-task");
		return taskScheduler;
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			//	storageService.deleteAll();
			storageService.init();
		};
	}


	public static void main(String[] args) {
		SpringApplication.run(SignApplication.class, args);
	}
}