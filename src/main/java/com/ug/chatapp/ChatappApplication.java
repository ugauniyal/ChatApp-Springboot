package com.ug.chatapp;


import org.quartz.core.QuartzScheduler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
public class ChatappApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ChatappApplication.class, args);
	}

}
