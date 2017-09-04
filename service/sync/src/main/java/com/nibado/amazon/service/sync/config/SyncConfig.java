package com.nibado.amazon.service.sync.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.nibado.amazon.lib.auth.PropertySecrets;
import com.nibado.amazon.lib.sync.Sync;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "sync")
public class SyncConfig {
    private String region;

    private List<Entry> entries = new ArrayList<>();

    public List<Entry> getEntries() {
        return entries;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public List<Sync> getSyncs() {
        log.info("Creating S3 client for region {}", region);
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        builder.setRegion(region);
        builder.setCredentials(new PropertySecrets());

        AmazonS3 client = builder.build();

        return getEntries()
                .stream()
                .map(e -> new Sync(client, e.bucket, e.key, toFile(e.directory)))
                .collect(Collectors.toList());
    }

    private static File toFile(final String dir) {
        String directory = dir.trim();
        if (directory.startsWith("~")) {
            directory = directory.replace("~", System.getProperty("user.home"));
        }

        File file = new File(directory);
        if (!file.exists()) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " does not exist");
        } else if (!file.isDirectory()) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " is not a directory");
        }

        return file;
    }

    @Data
    public static class Entry {
        private String directory;
        private String bucket;
        private String key;
    }
}
