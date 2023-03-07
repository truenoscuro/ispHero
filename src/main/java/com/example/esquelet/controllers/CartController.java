package com.example.esquelet.controllers;


import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.ServiceDTO;
import com.example.esquelet.dtos.TranslateDTO;
import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.models.Cart;
import com.example.esquelet.models.IdCart;
import com.example.esquelet.services.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage","articleComplete","urlCdn"})

public class CartController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TranslateService translateService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ServService servService;

    @Autowired
    private DomainRegisteredService domainRegisteredService;

    private void initCart(Model model){
        if(!model.containsAttribute("cartUser"))
            model.addAttribute("cartUser",new Cart());

    }

    private ArticleDTO chargeArticleBuy(ArticleDTO articleBuy){
        ArticleDTO article = articleService.getArticleDTO(articleBuy.getProduct());
        article.setDatesBuy(articleBuy);
        return article;
    }

    @GetMapping("/cart")
    public String view(Model model){
        initCart( model );
        ((Cart) model.getAttribute("cartUser")).getArticles()
                .forEach( article ->
                        translateService.translate(
                                article,
                                (TranslateDTO) Objects.requireNonNull(model.getAttribute("langPa" +
                                        "ge"))
                                )
                );
        return "backendUser/cartpage";
    }

    @GetMapping("/cart/{page}")
    public String redirect( @PathVariable String page ){
        return "redirect:/cart";
    }

    @PostMapping("/cart/vincule")
    public String vincule(@ModelAttribute("idCart") Long idCartArticle,Model model){
        Long idCart = ((IdCart) model.getAttribute("articleComplete")).getId();
        ArticleDTO article = ((Cart) model.getAttribute("cartUser")).getArticle( idCart );
        article.getProperty().replace("needDomain","false");
        article.setDomainAppend( ((Cart) model.getAttribute("cartUser")).getArticle( idCartArticle ) );
        return "redirect:/cart";
    }
    @PostMapping("/cart/addDomain")
    public String addDomain( @RequestParam("idCart") Long articleComplete , Model model ){
        ((IdCart) model.getAttribute("articleComplete")).setId(articleComplete);
        return "redirect:/account/services";
    }


    @PostMapping("/cart/add")
    public String addArticle(@ModelAttribute ArticleDTO articleBuy ,
                             Model model){
        System.out.println(articleBuy);

        initCart( model );
        ArticleDTO article = chargeArticleBuy( articleBuy );

        if( article.isDomain() ) {
            article.setDomainNameAndName(articleBuy.getDomainName());
            article.addProperty( "needDomain" , "false" );
        } else {
            article.addProperty( "needDomain" , "true" );
        }

        Cart cart =  ((Cart) model.getAttribute("cartUser"));

        if( model.containsAttribute("articleComplete")){
            Long idCart = ((IdCart) model.getAttribute("articleComplete")).getId();
            if(idCart != null) {
                ArticleDTO articleAppend = cart.getArticle(idCart);
                articleAppend.getProperty().replace("needDomain", "false");
                articleAppend.setDomainAppend(article);
            }
        }
        System.out.println(article);
        cart.add( article );
        return "redirect:/cart";
    }


    @PostMapping("/cart/remove")
    public String removeArticle(@RequestParam("idCart") Long idCart , Model model){
        Cart cart = (Cart) model.getAttribute("cartUser");
        ArticleDTO article = cart.getArticle(idCart);
        if( article.isDomain() ){
            cart.getArticles().stream()
                    .filter( a ->{
                        if( a.isDomain() ) return false;
                        if(a.getDomainAppend() == null ) return false;
                        return Objects.equals(a.getDomainAppend().getIdCart(), article.getIdCart());
                    }).forEach( a -> {
                        a.getProperty().replace("needDomain","true");
                        a.setDomainAppend( null );
                    } );
        }
        cart.remove( idCart );
        return "redirect:/cart";
    }
    @GetMapping("/cart/removeall")
    public String removeAll( Model model ){
        initCart( model );
        ((Cart) model.getAttribute("cartUser")).removeAll();
        return "redirect:/cart";
    }
    @Transactional
    @GetMapping("/cart/buy")
    public String buy(Model model){
        UserDTO user = (UserDTO) model.getAttribute("user");
        Cart cart = (Cart) model.getAttribute("cartUser");
        //convert in english cart;
        cart.getArticles().forEach( article -> translateService.translate(
                article,
                ((List<TranslateDTO>) model.getAttribute("languages")).stream()
                        .filter(language -> language.getCode().equals("en"))
                        .findFirst().get()
                )
        );
        //add Service
        servService.addServicesByUser ( user , cart );
        // add Invoice
        invoiceService.addInvoiceByUser( user , cart );
        //update DomainRegistered
        domainRegisteredService.updateDomainRegisteredWithCart( cart );

        cart.removeAll();
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

