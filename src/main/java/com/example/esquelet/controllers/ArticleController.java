package com.example.esquelet.controllers;


import com.example.esquelet.entities.Article;
import com.example.esquelet.models.ArticleSell;
import com.example.esquelet.repositories.ArticleRepository;
import com.example.esquelet.services.ArticleSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ArticleController {

    @Autowired
    ArticleSellService articleSellService;


    @GetMapping(value = "article/{idCategory}") // can pass product?
    public String showByProduct(@PathVariable("idCategory") String id, Model model ){

        //List<Article> articleList = articleRepository.findAllByProduct();
        List<ArticleSell> articleSellList = articleSellService.getListArticleSellList( id );



        return "article";
    }

    @GetMapping(value = "domaincheck")
    public String domainCheck(Model model ){
        model.addAttribute("domainName", "domainName");
        model.addAttribute("pageTitle", "Login");
        return "domaincheck";
    }

}
