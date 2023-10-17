package com.epiceros.library.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private FineProcessingService fineProcessingService;

    @Scheduled(cron = "0 0 0 * * ?") // This cron expression runs the task at midnight every day
    public void processFinesDaily() {
        fineProcessingService.processFines();
    }
}