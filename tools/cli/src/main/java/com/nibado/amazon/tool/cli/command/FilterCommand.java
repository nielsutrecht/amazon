package com.nibado.amazon.tool.cli.command;

import com.nibado.amazon.tool.cli.model.ObjectSummaryList;

import java.util.stream.Collectors;

public class FilterCommand implements Command {
    private final String regex;

    public FilterCommand(final String regex) {
        this.regex = regex;
    }

    @Override
    public Result<?> run(final Result<?> previous) {
        if (previous.is(ObjectSummaryList.class)) {
            return new Result<>(filter((ObjectSummaryList) previous.getValue()));
        } else {
            return Result.fail();
        }
    }

    private ObjectSummaryList filter(final ObjectSummaryList list) {
        return new ObjectSummaryList(
                list
                        .getObjects()
                        .stream()
                        .filter(o -> o.getKey().matches(regex))
                        .collect(Collectors.toList())
        );
    }
}
