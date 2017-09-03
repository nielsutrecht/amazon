package com.nibado.amazon.tool.cli.model;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class ObjectSummaryList implements KeyList {
    private final List<S3ObjectSummary> objects;

    @Override
    public List<Key> keys() {
        return objects.stream().map(S3ObjectSummaryKey::new).collect(Collectors.toList());
    }

    private static class S3ObjectSummaryKey implements Key {
        private final String key;
        private final String bucket;

        public S3ObjectSummaryKey(final S3ObjectSummary summary) {
            this.key = summary.getKey();
            this.bucket = summary.getBucketName();
        }

        @Override
        public String key() {
            return key;
        }

        @Override
        public String bucket() {
            return bucket;
        }
    }
}
