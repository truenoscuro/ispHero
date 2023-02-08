package com.example.esquelet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvnConfiguration implements WebMvcConfigurer {

    @Autowired
    @Qualifier("InterceptorB")
    private AuthInterceptorB authInterceptor;

    @Autowired
    @Qualifier("Translate")
    private TranslateInterceptor translateInterceptor;

    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/account", "/account/**", "/logout");
        registry.addInterceptor(translateInterceptor);
    }

    @Override
    public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowedHeaders("*");
    }

}