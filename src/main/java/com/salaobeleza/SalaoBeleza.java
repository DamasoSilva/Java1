package com.salaobeleza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SalaoBelezaApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SalaoBelezaApp.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SalaoBelezaApp.class);
    }
}