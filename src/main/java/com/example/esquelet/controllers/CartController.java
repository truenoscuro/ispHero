package com.example.esquelet.controllers;

import com.example.esquelet.models.ArticleSell;
import com.example.esquelet.models.Cart;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CartController {

    @PostMapping("/")
    public String addArticle(@ModelAttribute("ArticleSell") ArticleSell articleSell, Model model, HttpSession session){
        Cart cart = ( Cart ) session.getAttribute("cart");
        Cart cartUser = new Cart();
        if( cart != null ) { cartUser = cart; }
        cartUser.add( articleSell );
        session.setAttribute("cart", cartUser );
        return "index";
    }

    @PostMapping("/")
    public String removeArticle(@ModelAttribute("ArticleSell") ArticleSell articleSell, Model model, HttpSession session){
        Cart cart = ( Cart ) session.getAttribute("cart");
        cart.remove(articleSell);
        return "index";
    }
    @GetMapping("/")
    public String removeAll( Model model, HttpSession session){
        session.invalidate();
        return "index";
    }



}