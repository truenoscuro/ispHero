package com.example.esquelet.config;

import com.example.esquelet.dtos.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
@Component("InterceptorB")
class AuthInterceptorB implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptorB.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        logger.info("Session: " + session.getAttribute("user"));
        Object o = session.getAttribute("user");
        if(session.getAttribute("user") != null && ((UserDTO) o).isValid()) return true;
        logger.info("Unauthorized access request");
        response.sendRedirect("/login");
        return false;
    }
}