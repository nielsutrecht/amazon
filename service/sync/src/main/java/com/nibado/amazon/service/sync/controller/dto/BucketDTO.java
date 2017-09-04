package com.nibado.amazon.service.sync.controller.dto;

import com.amazonaws.services.s3.model.Bucket;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nibado.amazon.service.shared.rest.Links;
import lombok.Data;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Data
public class BucketDTO {
    private final String name;
    private final ZonedDateTime created;
    private final String owner;
    @JsonProperty("_links")
    private final Links links;

    public static BucketDTO from(final Bucket bucket) {
        return new BucketDTO(
                bucket.getName(),
                ZonedDateTime.ofInstant(bucket.getCreationDate().toInstant(), ZoneOffset.UTC),
                bucket.getOwner().getDisplayName(),
                createLinks(bucket));
    }

    private static Links createLinks(final Bucket bucket) {
        return new Links().self("/bucket/" + bucket.getName());
    }
}
