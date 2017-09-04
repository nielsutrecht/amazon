package com.nibado.amazon.service.sync.controller;

import com.nibado.amazon.service.sync.SyncStatus;
import com.nibado.amazon.service.sync.controller.dto.FileAndKeyDTO;
import com.nibado.amazon.service.sync.controller.dto.ObjectDTO;
import com.nibado.amazon.service.sync.service.SyncService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sync")
public class SyncController {
    private final SyncService service;

    @Autowired
    public SyncController(final SyncService service) {
        this.service = service;
    }

    @GetMapping("/log")
    public LogResponse log() {
        return new LogResponse(service.getRuns());
    }

    @GetMapping("/files")
    public FilesResponse files() {
        return new FilesResponse(service.getFiles()
                .stream()
                .map(FileAndKeyDTO::from)
                .collect(Collectors.toList()));
    }

    @GetMapping("/objects")
    public ObjectsResponse objects() {
        return new ObjectsResponse(service.getObjects()
                .stream()
                .map(ObjectDTO::from)
                .collect(Collectors.toList()));
    }

    @GetMapping("/list")
    public FilesResponse syncList() {
        return new FilesResponse(service.getSyncList()
                .stream()
                .map(FileAndKeyDTO::from)
                .collect(Collectors.toList()));
    }

    @GetMapping("/status")
    public StatusResponse status() {
        return new StatusResponse(service.getStatus());
    }

    @PostMapping
    public StatusResponse run() {
        return new StatusResponse(service.run());
    }

    @Data
    public static class LogResponse {
        private final List<ZonedDateTime> runs;
    }

    @Data
    public static class FilesResponse {
        private final List<FileAndKeyDTO> files;
    }

    @Data
    public static class ObjectsResponse {
        private final List<ObjectDTO> objects;
    }

    @Data
    public static class StatusResponse {
        private final SyncStatus status;
    }
}
