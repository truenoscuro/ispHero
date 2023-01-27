package com.example.esquelet.config;

import com.example.esquelet.dtos.TranslateDTO;
import com.example.esquelet.dtos.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
@Component("Translate")
class TranslateInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if(session.getAttribute("languages")!= null) return true;
        session.setAttribute("languages",
                Arrays.asList(
                        new TranslateDTO("en","English"),
                        new TranslateDTO("de","Deutsch"),
                        new TranslateDTO("es","Español"),
                        new TranslateDTO("fr","Français")
                ));
        return true;
    }
}