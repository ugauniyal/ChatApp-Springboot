package com.ug.chatapp.config;


import com.ug.chatapp.EmailJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail helloJobDetail() {
        return JobBuilder.newJob(EmailJob.class)
                .withIdentity("helloJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger helloJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5)
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(helloJobDetail())
                .withIdentity("helloJobTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}