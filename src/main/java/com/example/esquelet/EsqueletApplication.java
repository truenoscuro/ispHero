package com.example.esquelet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class EsqueletApplication extends org.springframework.boot.web.servlet.support.SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EsqueletApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EsqueletApplication.class);
    }

}
