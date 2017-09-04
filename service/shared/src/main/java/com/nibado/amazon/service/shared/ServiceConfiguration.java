package com.nibado.amazon.service.shared;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.nibado.amazon.service.shared.rest.Links;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ServiceConfiguration {
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();

        b.serializerByType(Duration.class, durationSerializer());
        b.serializerByType(ZonedDateTime.class, zonedDateTimeSerializer());
        b.serializerByType(ZonedDateTime.class, zonedDateTimeSerializer());
        b.serializerByType(LocalDate.class, localDateSerializer());
        b.serializerByType(LocalDateTime.class, localDateTimeSerializer());
        b.serializerByType(Links.class, Links.serializer());
        return b;
    }

    public static JsonSerializer<Duration> durationSerializer() {
        return new JsonSerializer<Duration>() {
            @Override
            public void serialize(Duration value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.getSeconds());
            }
        };
    }

    public static JsonSerializer<ZonedDateTime> zonedDateTimeSerializer() {
        return new JsonSerializer<ZonedDateTime>() {
            @Override
            public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            }
        };
    }

    public static JsonSerializer<LocalDateTime> localDateTimeSerializer() {
        return new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        };
    }

    public static JsonSerializer<LocalDate> localDateSerializer() {
        return new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        };
    }
}
