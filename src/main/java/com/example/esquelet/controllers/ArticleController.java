package com.example.esquelet.controllers;


import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.DomainRegisteredDTO;
import com.example.esquelet.services.ArticleService;
import com.example.esquelet.services.DomainRegisteredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage"})
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private DomainRegisteredService domainRegisteredService;

    @GetMapping(value = "/product/{category}") // can pass product?
    public String showByProduct(@PathVariable String category, Model model ){;
        model.addAttribute("articles",articleService.getArticleDTOList( category ));
        model.addAttribute("articleBuy",new ArticleDTO() );
        return "product/"+category;
    }

    @PostMapping("/domaincheck")
    public String  domainCheck(@RequestParam("domainSearch") String domainName, Model model ){
        List<ArticleDTO> articles = articleService.getArticleDTOList( "domain" );
        DomainRegisteredDTO domainRegistered = domainRegisteredService.getDomainRegisteredDTO(domainName);
/*
        System.out.println(domainRegistered);

        articles.stream().filter(domainRegistered::containTld).toList().forEach(System.out::println);
 */     model.addAttribute("domainName" , domainName );
        model.addAttribute(
                "articles",
                articles.stream().peek(article ->{
                    if (domainRegistered.containTld(article)) {
                        article.addProperty("taken", "true");
                    } else {
                        article.addProperty("taken", "false");
                    }
                }).toList()
        );
        return "domaincheck";
    }



}
