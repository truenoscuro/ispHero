package com.example.esquelet.controllers;


import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.models.Cart;
import com.example.esquelet.services.*;
import jakarta.transaction.Transactional;
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

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ServService servService;

    @Autowired
    private DomainRegisteredService domainRegisteredService;

    private void initCart(Model model){
        if(model.containsAttribute("cartUser")) return;
        model.addAttribute("cartUser",new Cart());
    }

    @GetMapping("/cart")
    public String view(Model model){
        initCart( model );
        return "backendUser/cartpage";
    }

    @PostMapping("/cart/add")
    public String addArticle(@ModelAttribute ArticleDTO articleBuy ,
                             Model model){
        initCart( model );
        ArticleDTO article = articleService.getArticleDTO(articleBuy.getProduct());
        if( article.getDomainName() != null ){
            article.setDomainName( articleBuy.getDomainName() + article.getProperty().get("tld") );
            article.setName( articleBuy.getDomainName());
        } else {

            // ARTICLE
            // is host or email
            // if not user --> return account

            // ARTICLE save
            // page
            // charge domainsServiceUser --> return domainNAme for buy
            // search domainCheck but need ARTICLE

        }
        ((Cart) model.getAttribute("cartUser")).add(article);
        return "redirect:/cart";
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
    @Transactional
    @GetMapping("/cart/buy")
    public String buy(Model model){
        UserDTO user = (UserDTO) model.getAttribute("user");
        Cart cart = (Cart) model.getAttribute("cartUser");
        //add Service
        servService.addServicesByUser(user,cart);
        // add Invoice
        invoiceService.addInvoiceByUser( user , cart );
        //update DomainRegistered
        domainRegisteredService.updateDomainRegisteredWithCart(cart);

        return "redirect:/cart/payment";
    }

    @GetMapping("/cart/payment")
    public String payment(Model model){
        initCart( model );
        model.addAttribute("cartUser",model.getAttribute("cartUser") );
        model.addAttribute("pageTitle","Payment");
        return "backendUser/payment";
    }

}

