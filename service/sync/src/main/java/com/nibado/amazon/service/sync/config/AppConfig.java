package com.nibado.amazon.service.sync.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Slf4j
@Configuration
@ComponentScan({"com.nibado.amazon.service.shared"})
public class AppConfig extends WebMvcConfigurerAdapter {

}
