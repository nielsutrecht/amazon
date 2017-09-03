package com.nibado.amazon.tool.cli.writer;

import com.nibado.amazon.tool.cli.command.Result;
import com.nibado.amazon.tool.cli.model.Key;
import com.nibado.amazon.tool.cli.model.KeyList;

import java.util.List;

public class ResultWriter {
    public static void write(final Result<?> result) {
        if (result.isEmpty()) {
            return;
        } else if (result.is(KeyList.class)) {
            List<Key> keys = result.as(KeyList.class).keys();
            keys.forEach(k -> System.out.println(k.key()));
            System.out.printf("%s keys total\n", keys.size());
        }
    }
}
