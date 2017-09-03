package com.nibado.amazon.tool.cli.command;

public interface Command {
    Result<?> run(Result<?> previous);
}
