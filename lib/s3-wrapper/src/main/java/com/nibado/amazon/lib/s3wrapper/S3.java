package com.nibado.amazon.lib.s3wrapper;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class S3 {
    private final AmazonS3 client;

    public S3(final AmazonS3 client) {
        this.client = client;
    }

    public ObjectListing listObjects(final String bucket) {
        return client.listObjects(bucket);
    }

    public ObjectListing listObjects(final String bucket, final String prefix) {
        return client.listObjects(bucket, prefix);
    }

    public String get(final String bucket, final String key) {
        S3Object object = client.getObject(bucket, key);

        return object.getObjectMetadata().getContentType();
    }

    public void delete(final String bucket, final String key) {
        client.deleteObject(bucket, key);
    }
}
