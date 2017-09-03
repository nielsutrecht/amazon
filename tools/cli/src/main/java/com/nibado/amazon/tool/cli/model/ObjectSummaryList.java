package com.nibado.amazon.tool.cli.model;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class ObjectSummaryList implements KeyList {
    private final List<S3ObjectSummary> objects;

    @Override
    public List<String> keys() {
        return objects.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }
}
