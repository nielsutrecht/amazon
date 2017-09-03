package com.nibado.amazon.service.browser.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Value;
import org.junit.Test;

public class LinksTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Links.class, Links.serializer());

        MAPPER.registerModule(module);
    }

    @Test
    public void test() throws Exception {
        Links links = new Links();
        links.rel("foo", "/foo");
        links.self("/bar");

        System.out.println(MAPPER.writeValueAsString(new TestClass(links)));
    }

    @Value
    public static class TestClass {
        @JsonProperty("_links")
        private Links links;
    }
}
