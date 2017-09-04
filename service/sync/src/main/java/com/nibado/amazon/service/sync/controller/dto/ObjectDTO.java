package com.nibado.amazon.service.sync.controller.dto;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nibado.amazon.service.shared.rest.Links;
import lombok.Data;

import javax.activation.MimetypesFileTypeMap;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Data
public class ObjectDTO {
    private final String key;
    private final String mimeType;
    private final long size;
    private final String eTag;
    private final String storageClass;
    private final ZonedDateTime lastModified;
    @JsonProperty("_links")
    private final Links links;

    public static ObjectDTO from(final S3ObjectSummary summary) {
        return new ObjectDTO(
                summary.getKey(),
                guessMimeType(summary),
                summary.getSize(),
                summary.getETag(),
                summary.getStorageClass(),
                ZonedDateTime.ofInstant(summary.getLastModified().toInstant(), ZoneOffset.UTC),
                createLinks(summary)
        );
    }

    private static String encode(final String s) {
        try {
            return URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static Links createLinks(final S3ObjectSummary summary) {
        return new Links().self("/object/" + summary.getBucketName() + "/" + summary.getKey());
    }

    private static String guessMimeType(final S3ObjectSummary summary) {
        return MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(summary.getKey());
    }
}
