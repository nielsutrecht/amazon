package com.nibado.amazon.service.sync.controller.dto;

import com.nibado.amazon.service.sync.service.domain.FileAndKey;
import lombok.Value;

@Value
public class FileAndKeyDTO {
    private String file;
    private String key;

    public static FileAndKeyDTO from(final FileAndKey fileAndKey) {
        return new FileAndKeyDTO(fileAndKey.getFile().getAbsolutePath(), fileAndKey.getKey());
    }
}
