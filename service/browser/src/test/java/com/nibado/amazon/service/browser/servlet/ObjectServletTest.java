package com.nibado.amazon.service.browser.servlet;

import org.junit.Test;

import javax.activation.MimetypesFileTypeMap;

public class ObjectServletTest {
    @Test
    public void mimeType() throws Exception {
        System.out.println(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType("/object/nibado-backup//sync/test-a/IMG_1599.JPG"));
    }
}
