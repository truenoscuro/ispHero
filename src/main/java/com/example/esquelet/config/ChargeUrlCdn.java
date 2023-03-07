package com.example.esquelet.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
@Component("ChargeUrlCdn")
public class ChargeUrlCdn implements HandlerInterceptor  {


    @Value("${url.urlcdn}")
    private String urlCdn;
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        HttpSession session =  request.getSession();
        if( session.getAttribute("urlCdn") == null ) session.setAttribute("urlCdn" , urlCdn );
        return true;
    }


}
