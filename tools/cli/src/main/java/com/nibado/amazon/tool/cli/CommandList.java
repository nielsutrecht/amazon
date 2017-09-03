package com.nibado.amazon.tool.cli;

import com.nibado.amazon.tool.cli.command.Command;
import com.nibado.amazon.tool.cli.command.Result;

import java.util.List;

public class CommandList implements Command {
    private final List<Command> commands;

    public CommandList(final List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public Result<?> run(final Result<?> previous) {
        Result<?> myPrevious = previous;

        for (Command c : commands) {
            if(myPrevious.isSuccess()) {
                myPrevious = c.run(myPrevious);
            } else {
                return Result.fail();
            }
        }

        return myPrevious;
    }
}
