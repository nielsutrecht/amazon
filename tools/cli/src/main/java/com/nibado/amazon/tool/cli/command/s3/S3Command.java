package com.nibado.amazon.tool.cli.command.s3;

import com.nibado.amazon.lib.s3wrapper.S3;
import com.nibado.amazon.tool.cli.command.Command;

public abstract class S3Command implements Command {
    protected final S3 s3;

    public S3Command(final S3 s3) {
        this.s3 = s3;
    }
}
