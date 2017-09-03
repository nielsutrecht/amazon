package com.nibado.amazon.tool.cli.command.s3;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.nibado.amazon.lib.s3wrapper.S3;
import com.nibado.amazon.tool.cli.command.Result;
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
        List<String> keys = key != null ? Collections.singletonList(key) : getKeyList(previous);

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

        return new Result<>(() -> keys);
    }

    private List<String> getKeyList(final Result<?> previous) {
        if (previous.is(KeyList.class)) {
            return previous
                    .as(KeyList.class)
                    .keys();
        }

        return Collections.emptyList();
    }

    private void deleteKeys(final List<String> keys) {
        for (String key : keys) {
            s3.delete(bucket, key);
            System.out.println("Deleted " + key);
        }
    }

    private static class DeletedKeyList implements KeyList {
        @Override
        public List<String> keys() {
            return null;
        }
    }
}
