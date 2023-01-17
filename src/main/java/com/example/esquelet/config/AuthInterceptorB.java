package com.example.esquelet.config;

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
class authInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(authInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            logger.info("Unauthorized access request");
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}