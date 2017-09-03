package com.nibado.amazon.tool.cli.command.s3;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectListing;
import com.nibado.amazon.lib.s3wrapper.S3;
import com.nibado.amazon.tool.cli.command.Result;
import com.nibado.amazon.tool.cli.model.ObjectSummaryList;

public class ListCommand extends S3Command {
    private final String bucket;
    private final String prefix;

    public ListCommand(final S3 s3, final String bucket, final String prefix) {
        super(s3);
        this.bucket = bucket;
        this.prefix = prefix;
    }

    @Override
    public Result<ObjectSummaryList> run(final Result<?> previous) {
        ObjectListing listing;
        try {
            if (prefix == null) {
                listing = s3.listObjects(bucket);
            } else {
                listing = s3.listObjects(bucket, prefix);
            }
        } catch (AmazonS3Exception e) {
            System.err.println(e.getMessage());
            return Result.fail();
        }

        return new Result<>(new ObjectSummaryList(listing.getObjectSummaries()));
    }
}
