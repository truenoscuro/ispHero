package com.example.esquelet.config;

import com.example.esquelet.dtos.TranslateDTO;
import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.services.TranslateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.net.http.HttpHeaders;
import java.util.*;


@Configuration
@Component("Translate")
class TranslateInterceptor implements HandlerInterceptor {

    @Autowired
    TranslateService translateService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //-- Code use
        String code = LocaleContextHolder.getLocale().toString();
        // init Languages
        HttpSession session = request.getSession();
        session.setAttribute("languages", translateService.getAllSimple()); //Todo languages
        // change Language page
        List<TranslateDTO> translates = (List<TranslateDTO>) session.getAttribute("languages");
        String finalCode = code;
        session.setAttribute("langPage", translates.stream()
                .filter(t -> t.getCode().equals(finalCode))
                .findFirst().get()
        );
        return true;
    }

}