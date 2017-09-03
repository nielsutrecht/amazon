package com.nibado.amazon.tool.cli.command.s3;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.nibado.amazon.lib.s3wrapper.S3;
import com.nibado.amazon.tool.cli.command.Result;
import com.nibado.amazon.tool.cli.model.Key;
import com.nibado.amazon.tool.cli.model.KeyList;

import java.util.Collections;
import java.util.List;

public class DeleteCommand extends S3Command {
    private final String bucket;
    private final String key;

    public DeleteCommand(final S3 s3, final String bucket, final String key) {
        super(s3);
        this.bucket = bucket;
        this.key = key;
    }

    @Override
    public Result<KeyList> run(final Result<?> previous) {
        List<Key> keys = key != null ? Collections.singletonList(new KeyAndBucket(key, bucket)) : getKeyList(previous);

        if (keys.isEmpty()) {
            System.err.println("Nothing to delete");
            return Result.fail();
        }

        try {
            deleteKeys(keys);
        } catch (AmazonS3Exception e) {
            System.err.println(e.getMessage());
            return Result.fail();
        }

        return new Result<>();
    }

    private List<Key> getKeyList(final Result<?> previous) {
        if (previous.is(KeyList.class)) {
            return previous
                    .as(KeyList.class)
                    .keys();
        }

        return Collections.emptyList();
    }

    private void deleteKeys(final List<Key> keys) {
        int count = 0;
        for (Key key : keys) {
            s3.delete(key.bucket(), key.key());
            System.out.printf("Deleted %s/%s\n", key.bucket(), key.key());
            count++;
        }
    }

    private static class KeyAndBucket implements Key {
        private final String key;
        private final String bucket;

        public KeyAndBucket(String key, String bucket) {
            this.key = key;
            this.bucket = bucket;
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
