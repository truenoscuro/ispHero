package com.example.esquelet.controller;

import ch.qos.logback.core.model.Model;
import com.example.esquelet.models.Article;
import com.example.esquelet.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;


    @GetMapping(value = "article/{producto}")
    public String showByProduct( Model model ){

        List<Article> articleList = articleRepository.findAllByProduct();



        return "article";
    }

}
