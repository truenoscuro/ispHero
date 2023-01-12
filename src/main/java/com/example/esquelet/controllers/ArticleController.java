package com.example.esquelet.controllers;


import com.example.esquelet.entities.Article;
import com.example.esquelet.models.ArticleSell;
import com.example.esquelet.repositories.ArticleRepository;
import com.example.esquelet.repositories.LanguageControler;
import com.example.esquelet.services.ArticleSellService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ArticleController {

    @Autowired
    ArticleSellService articleSellService;
    @Autowired
    LanguageControler languageControler;


    @GetMapping(value = "article/{idCategory}") // can pass product?
    public String showByProduct(@PathVariable("idCategory") String id, Model model ){

        //List<Article> articleList = articleRepository.findAllByProduct();
        List<ArticleSell> articleSellList = articleSellService.getListArticleSellList( id );



        return "article";
    }

    @PostMapping("/domaincheck")
    public String  domainCheck(@RequestParam("domainSearch") String domainName,
                              Model model ){
        model.addAttribute("domainName" , domainName );
        model.addAttribute("articleSellList",articleSellService.getListArticleSellList( "1" ));
        model.addAttribute("languages",languageControler.findAll() );
        return "domaincheck";
    }

}
