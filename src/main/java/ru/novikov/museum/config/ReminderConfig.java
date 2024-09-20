package ru.novikov.museum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ReminderConfig {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
