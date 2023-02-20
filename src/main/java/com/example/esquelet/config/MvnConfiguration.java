package com.example.esquelet.config;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvnConfiguration implements WebMvcConfigurer {

    @Autowired
    @Qualifier("InterceptorB")
    private AuthInterceptorB authInterceptor;

    @Autowired
    @Qualifier("Translate")
    private TranslateInterceptor translateInterceptor;

    @Autowired
    @Qualifier("ArticleCompleteInterceptor")
    private  ArticleCompleteInterceptor articleCompleteInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //--
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/account", "/account/**", "/logout");
        //--
        registry.addInterceptor(translateInterceptor);
        //--
        registry.addInterceptor(articleCompleteInterceptor)
                .addPathPatterns("/","/product/**")
                .excludePathPatterns("/product/search");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowedHeaders("*");
    }

}