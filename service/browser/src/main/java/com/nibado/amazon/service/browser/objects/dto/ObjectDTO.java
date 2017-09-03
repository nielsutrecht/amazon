package com.nibado.amazon.service.browser.objects.dto;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Data
public class ObjectDTO {
    private final String key;
    private final String encodedKey;
    private final long size;
    private final String eTag;
    private final String storageClass;
    private final ZonedDateTime lastModified;

    public static ObjectDTO from(final S3ObjectSummary summary) {
        return new ObjectDTO(
                summary.getKey(),
                encode(summary.getKey()),
                summary.getSize(),
                summary.getETag(),
                summary.getStorageClass(),
                ZonedDateTime.ofInstant(summary.getLastModified().toInstant(), ZoneOffset.UTC));
    }

    private static String encode(final String s) {
        try {
            return URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
