package com.example.esquelet.controllers;

import com.example.esquelet.entities.User;
import com.example.esquelet.models.ArticleSell;
import com.example.esquelet.models.Cart;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class CartController {

    @PostMapping("/{articleSell}")
    public String addArticle(@ModelAttribute("articleSell") ArticleSell articleSell, Model model, HttpSession session){
        Cart cart = ( Cart ) session.getAttribute("cart");
        Cart cartUser = new Cart();
        if( cart != null ) { cartUser = cart; }
        cartUser.add( articleSell );
        session.setAttribute("cart", cartUser );
        return "index";
    }
}
