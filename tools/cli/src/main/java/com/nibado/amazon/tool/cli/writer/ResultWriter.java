package com.nibado.amazon.tool.cli.writer;

import com.nibado.amazon.tool.cli.command.Result;
import com.nibado.amazon.tool.cli.model.KeyList;

public class ResultWriter {
    public static void write(final Result<?> result) {
        if (result.isEmpty()) {
            return;
        } else if (result.is(KeyList.class)) {
            result.as(KeyList.class).keys().forEach(System.out::println);
        }
    }
}
