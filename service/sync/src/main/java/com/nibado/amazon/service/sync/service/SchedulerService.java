package com.nibado.amazon.service.sync.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchedulerService {
    private static final String CRON = "0 */15 * * * *";

    private final SyncService service;

    @Autowired
    public SchedulerService(final SyncService service) {
        this.service = service;
    }

    @Scheduled(cron = CRON)
    public void run() {
        log.info("Scheduled service run, status: ", service.run());
    }
}
