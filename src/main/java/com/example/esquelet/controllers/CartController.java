package com.example.esquelet.controllers;


import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.models.Cart;
import com.example.esquelet.repositories.LanguageRepository;
import com.example.esquelet.services.ArticleService;
import com.example.esquelet.services.TranslateService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage"})

public class CartController {

    @Autowired
    private ArticleService articleService;


    @PostMapping("/cartpage")
    public String addArticle(@ModelAttribute ArticleDTO articleBuy ,
                             Model model){
        if(!model.containsAttribute("cartUser")){
            model.addAttribute("cartUser",new Cart());
        }
        ArticleDTO article = articleService.getArticleDTO(articleBuy.getProduct());
        article.setDomainName(articleBuy.getDomainName());
        ((Cart) model.getAttribute("cartUser")).add(article);
        return "cartpage";


    }
    @GetMapping("/cartpage")
    public String view(Model model){
        if(!model.containsAttribute("cartUser")){
            model.addAttribute("cartUser",new Cart());
        }
        return "cartpage";
    }

    @PostMapping("/remove")
    public String removeArticle(@RequestParam("product") String product , Model model){
        ((Cart) model.getAttribute("cartUser")).remove(articleService.getArticleDTO( product ));
        return "redirect:/cartpage";
    }
    @GetMapping("/removeall")
    public String removeAll( Model model ){
        ((Cart) model.getAttribute("cartUser")).removeAll();
        return "redirect:/cartpage";
    }



}

