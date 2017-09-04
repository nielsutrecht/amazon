package com.nibado.amazon.service.sync.service;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.nibado.amazon.lib.sync.Sync;
import com.nibado.amazon.service.sync.SyncStatus;
import com.nibado.amazon.service.sync.config.SyncConfig;
import com.nibado.amazon.service.sync.service.domain.FileAndKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SyncService {
    private final List<Sync> syncs;
    private final List<ZonedDateTime> runs = new ArrayList<>();
    private final AtomicBoolean running = new AtomicBoolean();

    private ExecutorService pool = Executors.newFixedThreadPool(1);

    public SyncService(final SyncConfig config) {
        syncs = config.getSyncs();
    }

    public SyncStatus run() {
        synchronized (running) {
            if (running.get()) {
                log.info("Sync already running.");
                return SyncStatus.ALREADY_RUNNING;
            } else {
                running.set(true);

                pool.submit(() -> {
                    syncs.forEach(Sync::syncAll);
                    running.set(false);
                    log.info("Sync finished.");
                });
                log.info("Sync started.");
                return SyncStatus.RUNNING;
            }
        }
    }

    public List<ZonedDateTime> getRuns() {
        return Collections.unmodifiableList(runs);
    }

    public List<FileAndKey> getFiles() {
        return syncs
                .stream()
                .flatMap(s -> s.list()
                        .stream()
                        .map(f -> new FileAndKey(f, s.toKey(f))))
                .collect(Collectors.toList());
    }

    public List<S3ObjectSummary> getObjects() {
        return syncs
                .stream()
                .flatMap(s -> s.s3List()
                        .stream())
                .collect(Collectors.toList());
    }

    public List<FileAndKey> getSyncList() {
        return syncs
                .stream()
                .flatMap(s -> s.syncList()
                        .stream()
                        .map(f -> new FileAndKey(f, s.toKey(f))))
                .collect(Collectors.toList());
    }

    public SyncStatus getStatus() {
        return running.get() ? SyncStatus.RUNNING : SyncStatus.INACTIVE;
    }
}
