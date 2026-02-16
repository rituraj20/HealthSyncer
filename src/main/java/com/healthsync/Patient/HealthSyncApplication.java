package com.healthsync.Patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.healthsync")
public class HealthSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthSyncApplication.class, args);
    }

}
