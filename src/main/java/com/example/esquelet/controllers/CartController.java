package com.example.esquelet.controllers;


import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.models.Cart;
import com.example.esquelet.repositories.LanguageRepository;
import com.example.esquelet.services.ArticleService;
import com.example.esquelet.services.TranslateService;
import com.example.esquelet.services.UserService;
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

    @Autowired
    private UserService userService;


    @PostMapping("/cart/add")
    public String addArticle(@ModelAttribute ArticleDTO articleBuy ,
                             Model model){
        if(!model.containsAttribute("cartUser")){
            model.addAttribute("cartUser",new Cart());
        }

        ArticleDTO article = articleService.getArticleDTO(articleBuy.getProduct());

        if(article.getDomainName() != null)
            article.setDomainName( articleBuy.getDomainName() + article.getProperty().get("tld") );
        System.out.println(article);
        ((Cart) model.getAttribute("cartUser")).add(article);
        return "redirect:/cart";


    }
    @GetMapping("/cart")
    public String view(Model model){
        if(!model.containsAttribute("cartUser")){
            model.addAttribute("cartUser",new Cart());
        }
        return "backendUser/cartpage";
    }

    @PostMapping("/cart/remove")
    public String removeArticle(@RequestParam("product") String product , Model model){
        ((Cart) model.getAttribute("cartUser")).remove( product );
        return "redirect:/cart";
    }
    @GetMapping("/cart/removeall")
    public String removeAll( Model model ){
        ((Cart) model.getAttribute("cartUser")).removeAll();
        return "redirect:/cart";
    }

    @GetMapping("/cart/buy")
    public String buy(Model model){
        userService.buy(
                (UserDTO) model.getAttribute("user"),
                (Cart) model.getAttribute("cartUser")
        );

        return "redirect:/cart/payment";
    }

    @GetMapping("/cart/payment")
    public String payment(Model model){
        if(!model.containsAttribute("cartUser")){
            model.addAttribute("cartUser",new Cart());
        }
        model.addAttribute("cartUser",(Cart) model.getAttribute("cartUser"));
        model.addAttribute("pageTitle","Payment");
        return "backendUser/payment";
    }

}

