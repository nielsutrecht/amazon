package com.nibado.amazon.service.sync;

import org.junit.Test;

import java.io.File;

public class SyncStatusTest {
    @Test
    public void test() {
        System.out.println(System.getProperty("user.home"));
        System.out.println(new File("~/tmp").getAbsolutePath());
    }
}
