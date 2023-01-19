package com.example.esquelet.controllers;


import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.repositories.LanguageControler;
import com.example.esquelet.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser"})
public class ArticleController {

    @Autowired
    ArticleService articleService;
    @Autowired
    LanguageControler languageControler;

    @GetMapping(value = "/product/{category}") // can pass product?
    public String showByProduct(@PathVariable String category, Model model ){;
        model.addAttribute("articles",articleService.getArticleDTOList( category ));
        model.addAttribute("languages",languageControler.findAll() );
        model.addAttribute("articleBuy",new ArticleDTO() );
        return "product/"+category;
    }

    @PostMapping("/domaincheck")
    public String  domainCheck(@RequestParam("domainSearch") String domainName, Model model ){
        model.addAttribute("domainName" , domainName );
        model.addAttribute("articleSellList", articleService.getArticleDTO( "domain" ));
        model.addAttribute("languages",languageControler.findAll() );
        return "domaincheck";
    }



}
