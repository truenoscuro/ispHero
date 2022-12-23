package com.example.esquelet.controllers;

import ch.qos.logback.core.model.Model;
import com.example.esquelet.entities.Article;
import com.example.esquelet.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;


    @GetMapping(value = "article/{id_product}") // can pass product?
    public String showByProduct(@PathVariable("id_product") String id, Model model ){

        //List<Article> articleList = articleRepository.findAllByProduct();



        return "article";
    }

}
