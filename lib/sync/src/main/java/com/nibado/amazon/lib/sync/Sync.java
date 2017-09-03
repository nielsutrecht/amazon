package com.nibado.amazon.lib.sync;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class Sync {
    private final String key;
    private final File directory;
    private final AmazonS3 client;
    private final String bucket;

    public Sync(final AmazonS3 client, final String bucket, final String key, final File directory) {
        this.client = client;
        this.key = key;
        this.directory = directory;
        this.bucket = bucket;
    }

    List<File> list() {
        List<File> files = new ArrayList<>();

        listFiles(directory, files);

        return files;
    }

    String toKey(final File file) {
        return String.format("/sync/%s/%s", key, directory.toPath().relativize(file.toPath()));
    }

    List<S3ObjectSummary> s3List() {
        log.info("Listing for bucket {} and prefix {}", bucket, "/sync/" + key);
        return client
                .listObjects(bucket, "/sync/" + key)
                .getObjectSummaries();
    }

    PutObjectResult putFile(final File file) throws IOException {

        String key = toKey(file);

        log.info("Uploading {} to bucket {} with key {}", file, bucket, key);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setLastModified(new Date(file.lastModified()));
        metadata.setContentLength(file.length());

        return client.putObject(bucket, key, new FileInputStream(file), metadata);
    }

    Optional<String> getEtag(final File file) {
        String key = toKey(file);

        try {
            S3Object object = client.getObject(bucket, key);
            log.info("Etag for file {} is {}", file, object.getObjectMetadata().getETag());
            return Optional.of(object.getObjectMetadata().getETag());
        } catch (AmazonS3Exception e) {
            if(e.getStatusCode() == 404) {
                return Optional.empty();
            } else {
                throw e;
            }
        }
    }

    void getFile(final String key, File toLocation) throws IOException {
        S3Object object = client.getObject(bucket, key);

        Files.copy(object.getObjectContent(), toLocation.toPath());
    }

    List<File> syncList() {
        List<File> files = list();

        Map<String, S3ObjectSummary> objectMap = s3List()
                .stream()
                .collect(Collectors.toMap(S3ObjectSummary::getKey, s -> s));

        List<File> dontExist = files
                .stream()
                .filter(f -> !objectMap.containsKey(toKey(f)))
                .collect(Collectors.toList());

        List<File> doExistButDifferentEtag = files
                .stream()
                .filter(f -> objectMap.containsKey(toKey(f)))
                .filter(f -> !etagMatches(f, objectMap))
                .collect(Collectors.toList());

        if(!doExistButDifferentEtag.isEmpty()) {
            log.warn("These files have been modified: {}", doExistButDifferentEtag);
        }

        return dontExist;
    }

    public void syncAll() {
        List<File> syncList = syncList();

        if(syncList.isEmpty()) {
            log.info("No files to sync");
        } else {
            log.info("{} files to sync", syncList.size());
        }

        sync(syncList);
    }

    public void sync(final List<File> files) {
        ExecutorService pool = Executors.newFixedThreadPool(4);

        List<File> failed = new ArrayList<>();

        for(File f : files) {
             pool.submit(() -> {
                 try {
                     putFile(f);
                 } catch (IOException e) {
                     failed.add(f);
                 }
             });
        }

        try {
            pool.awaitTermination(16, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pool.shutdown();

        if(!failed.isEmpty()) {
            log.warn("Failed to upload {} files: {}", failed.size(), failed);
        }
    }

    void delete(final String key) {
        client.deleteObject(bucket, key);
    }

    void delete(final File file) {
        delete(toKey(file));
    }

    ObjectMetadata getMetaData(final File file) {
        return client.getObjectMetadata(bucket, toKey(file));
    }

    private boolean dateMatches(final File file, final Map<String, S3ObjectSummary> objectMap) {
        String key = toKey(file);
        S3ObjectSummary summary = objectMap.get(key);

        return summary.getLastModified().equals(new Date(file.lastModified()));
    }

    private boolean etagMatches(final File file, final Map<String, S3ObjectSummary> objectMap) {
        String key = toKey(file);
        S3ObjectSummary summary = objectMap.get(key);

        try {
            return MD5.md5(file).equals(summary.getETag());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void listFiles(final File directory, final List<File> fileList) {
        if(!directory.isDirectory()) {
            throw new IllegalArgumentException(directory + " is not a directory");
        }

        for(File f : directory.listFiles()) {
            if(f.isDirectory()) {
                listFiles(f, fileList);
            } else {
                fileList.add(f);
            }
        }
    }
}
