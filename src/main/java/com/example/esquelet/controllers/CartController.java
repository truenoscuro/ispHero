package com.example.esquelet.controllers;

import com.example.esquelet.models.ArticleSell;
import com.example.esquelet.models.Cart;
import com.example.esquelet.repositories.LanguageControler;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CartController {
    @Autowired
    LanguageControler languageControler;
    @PostMapping("/c")
    public String addArticle(@ModelAttribute("ArticleSell") ArticleSell articleSell, Model model, HttpSession session, @RequestParam(name = "lang", required = false) String lang){
        Cart cart = ( Cart ) session.getAttribute("cart");
        model.addAttribute("languages",languageControler.findAll() );
        model.addAttribute("language", languageControler.findByCode(lang));
        Cart cartUser = new Cart();
        if( cart != null ) { cartUser = cart; }
        cartUser.add( articleSell );
        session.setAttribute("cart", cartUser );
        if (session.getAttribute("user") != null) {
            model.addAttribute("isLogged", true);
        }
        return "index";
    }

    @PostMapping("/a")
    public String removeArticle(@ModelAttribute("ArticleSell") ArticleSell articleSell, Model model, HttpSession session, @RequestParam(name = "lang", required = false) String lang){
        Cart cart = ( Cart ) session.getAttribute("cart");
        model.addAttribute("languages",languageControler.findAll() );
        model.addAttribute("language", languageControler.findByCode(lang));
        if (session.getAttribute("user") != null) {
            model.addAttribute("isLogged", true);
        }
        cart.remove(articleSell);
        return "index";
    }
    @GetMapping("/b")
    public String removeAll( Model model, HttpSession session, @RequestParam(name = "lang", required = false) String lang){
        model.addAttribute("languages",languageControler.findAll() );
        model.addAttribute("language", languageControler.findByCode(lang));
        if (session.getAttribute("user") != null) {
            model.addAttribute("isLogged", true);
        }
        session.invalidate();
        return "index";
    }



}
