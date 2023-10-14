package com.ug.chatapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup
        implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */

    @Value("${spring.application.name:Default Demo App}")
    private String name;

    @Value("${spring.mail.username}")
    private String email;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        // here your code ...
        System.out.println("Hello");
        System.out.println(name);
        System.out.println(email);

        return;
    }
}