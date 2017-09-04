package com.nibado.amazon.service.sync.service.domain;

import lombok.Value;

import java.io.File;

@Value
public class FileAndKey {
    private final File file;
    private final String key;
}
