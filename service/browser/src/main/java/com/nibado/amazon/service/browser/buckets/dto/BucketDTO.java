package com.nibado.amazon.service.browser.buckets.dto;

import lombok.Data;

@Data
public class BucketDTO {
    private final String name;

    public static BucketDTO of(final String name) {
        return new BucketDTO(name);
    }
}
