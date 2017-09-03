package com.nibado.amazon.tool.cli.command;

public class FailCommand implements Command {
    @Override
    public Result<?> run(final Result<?> previous) {
        return Result.fail();
    }
}
