package com.nibado.amazon.service.browser.config;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.nibado.amazon.lib.auth.PropertySecrets;
import com.nibado.amazon.lib.s3wrapper.S3;
import com.nibado.amazon.service.browser.rest.Links;
import com.nibado.amazon.service.browser.servlet.ObjectServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {
    @Value("${amazon.region}")
    private String region;

    @Bean
    public S3 s3() {
        log.info("Creating S3 wrapper for region {}", region);
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        builder.setRegion(region);
        builder.setCredentials(new PropertySecrets());

        return new S3(builder.build());
    }

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

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new ObjectServlet(), "/object/*");
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
