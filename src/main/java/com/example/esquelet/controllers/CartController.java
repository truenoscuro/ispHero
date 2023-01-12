package com.example.esquelet.controllers;

import com.example.esquelet.models.ArticleSell;
import com.example.esquelet.models.Cart;
import com.example.esquelet.repositories.LanguageControler;
import com.example.esquelet.services.ArticleSellService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("cartUser")
public class CartController {
    @Autowired
    ArticleSellService articleSellService;
    @Autowired
    LanguageControler languageControler;

    @PostMapping("/cartpage")
    public String addArticle( @RequestParam("productBuy") String productBuy ,
                              Model model){

        System.out.println(model.containsAttribute("cartUser"));
        if(!model.containsAttribute("cartUser")){
            model.addAttribute("cartUser",new Cart());
        }
        ((Cart) model.getAttribute("cartUser")).add(articleSellService.getArticleSell(productBuy));
        //System.out.println(model.getAttribute("cart"));
        model.addAttribute("languages",languageControler.findAll() );
        return "cartpage";


    }

    @PostMapping("/a")
    public String removeArticle(@ModelAttribute("ArticleSell") ArticleSell articleSell, Model model, HttpSession session){
        Cart cart = ( Cart ) session.getAttribute("cart");
        cart.remove(articleSell);
        return "index";
    }
    @GetMapping("/b")
    public String removeAll( Model model, HttpSession session){
        session.invalidate();
        return "index";
    }



}
