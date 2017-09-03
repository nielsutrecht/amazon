package com.nibado.amazon.tool.cli;

import com.nibado.amazon.lib.s3wrapper.S3;
import com.nibado.amazon.tool.cli.command.Command;
import com.nibado.amazon.tool.cli.command.ExitCommand;
import com.nibado.amazon.tool.cli.command.FilterCommand;
import com.nibado.amazon.tool.cli.command.Result;
import com.nibado.amazon.tool.cli.command.s3.DeleteCommand;
import com.nibado.amazon.tool.cli.command.s3.ListCommand;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandFactory {
    private static final Pattern LIST_PATTERN = Pattern.compile("list (.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern DELETE_PATTERN = Pattern.compile("del (.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern FILTER_PATTERN = Pattern.compile("filter (.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern EXIT_PATTERN = Pattern.compile("exit.*", Pattern.CASE_INSENSITIVE);

    private final S3 s3;

    public CommandFactory(final S3 s3) {
        this.s3 = s3;
    }

    public Command parse(final String line) {
        System.out.println("parsing " + line);
        List<Command> commands = Stream
                .of(line.split("\\|"))
                .map(String::trim)
                .map(this::mapToCommand)
                .collect(Collectors.toList());

        return new CommandList(commands);
    }

    private Command mapToCommand(final String cmd) {
        System.out.println("mapping " + cmd);
        Matcher m = LIST_PATTERN.matcher(cmd);
        if(m.matches()) {
            return new ListCommand(s3, m.group(1), null);
        }

        m = DELETE_PATTERN.matcher(cmd);
        if(m.matches()) {
            return new DeleteCommand(s3, m.group(1), null);
        }

        m = FILTER_PATTERN.matcher(cmd);
        if(m.matches()) {
            return new FilterCommand(m.group(1));
        }

        m = EXIT_PATTERN.matcher(cmd);

        if(m.matches()) {
            return new ExitCommand();
        }

        return new UnknownCommand(cmd);
    }

    private static class UnknownCommand implements Command {
        private final String command;

        public UnknownCommand(final String command) {
            this.command = command;
        }

        @Override
        public Result<?> run(final Result<?> previous) {
            throw new IllegalArgumentException("Unknown command: " + command);
        }
    }
}
