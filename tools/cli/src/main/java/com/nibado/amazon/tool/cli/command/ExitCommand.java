package com.nibado.amazon.tool.cli.command;

public class ExitCommand implements Command {
    @Override
    public Result<?> run(final Result<?> previous) {
        System.exit(0);

        return new Result<>();
    }
}
