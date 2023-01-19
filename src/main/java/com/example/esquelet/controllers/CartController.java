package com.example.esquelet.controllers;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.models.Cart;
import com.example.esquelet.repositories.LanguageControler;
import com.example.esquelet.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@SessionAttributes("cartUser")
public class CartController {
    @Autowired
    ArticleService articleService;
    @Autowired
    LanguageControler languageControler;

    @PostMapping("/cartpage")
    public String addArticle(@RequestParam( "product"  ) String product ,
                             Model model){
        if(!model.containsAttribute("cartUser")){
            model.addAttribute("cartUser",new Cart());
        }
        ((Cart) model.getAttribute("cartUser")).add(articleService.getArticleDTO(product));
        model.addAttribute("languages",languageControler.findAll() );
        return "cartpage";


    }
    @GetMapping("/cartpage")
    public String view(Model model){
        if(!model.containsAttribute("cartUser")){
            model.addAttribute("cartUser",new Cart());
        }
        model.addAttribute("languages",languageControler.findAll() );
        return "cartpage";
    }

    @PostMapping("/remove")
    public String removeArticle(@RequestParam("product") String product , Model model){
        ((Cart) model.getAttribute("cartUser")).remove(articleService.getArticleDTO( product ));
        return "redirect:/cartpage";
    }
    @GetMapping("/removeall")
    public String removeAll( Model model){
        ((Cart) Objects.requireNonNull(model.getAttribute("cartUser"))).removeAll();
        return "redirect:/cartpage";
    }



}

