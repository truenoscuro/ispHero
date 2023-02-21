package com.example.esquelet.config;

import com.example.esquelet.models.IdCart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
@Component("ArticleCompleteInterceptor")
public class ArticleCompleteInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        HttpSession session = request.getSession( ) ;
        session.setAttribute("articleComplete",new IdCart() );
        return true;
    }

}
