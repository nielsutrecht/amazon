package com.nibado.amazon.service.browser.config;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.nibado.amazon.lib.auth.PropertySecrets;
import com.nibado.amazon.lib.s3wrapper.S3;
import com.nibado.amazon.service.browser.servlet.ObjectServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Slf4j
@Configuration
@ComponentScan({"com.nibado.amazon.service.shared"})
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
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new ObjectServlet(), "/object/*");
    }
}
