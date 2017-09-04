package com.nibado.amazon.service.shared.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Links {
    private List<Link> links = new ArrayList<>();

    public Links rel(final String name, final String href) {
        this.links.add(new Link(name, href));

        return this;
    }

    public Links self(final String href) {
        return rel("self", href);
    }

    @Value
    public static class Link {
        private final String name;
        private final String href;
    }

    public static JsonSerializer<Links> serializer() {
        return new JsonSerializer<Links>() {
            @Override
            public void serialize(Links links, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
                gen.writeStartObject();
                for(Link l : links.links) {
                    gen.writeObjectFieldStart(l.name);
                    gen.writeFieldName("href");
                    gen.writeString(l.href);
                    gen.writeEndObject();
                }
                gen.writeEndObject();
            }
        };
    }
}
