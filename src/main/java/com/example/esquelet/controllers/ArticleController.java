package com.example.esquelet.controllers;


import com.example.esquelet.dtos.*;

import com.example.esquelet.repositories.WaitingDomainRepository;
import com.example.esquelet.services.ArticleService;
import com.example.esquelet.services.DomainRegisteredService;

import com.example.esquelet.services.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage"})
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private DomainRegisteredService domainRegisteredService;

    @Autowired
    private TranslateService translateService;
    @Autowired
    private WaitingDomainRepository waitingDomainRepository;


    private void addDomainNameAndName( List<ArticleDTO> articles , String domainName ){
        articles.forEach( article ->  article.setDomainNameAndName(domainName) );
    }

    private void addHasTaken( List<ArticleDTO> articles , String domainName ){
        DomainRegisteredDTO domainRegistered = domainRegisteredService.getDomainRegisteredDTO( domainName );
        articles.forEach(article ->{
                    if (domainRegistered.containTld(article)) article.addProperty("taken", "true");
                    else article.addProperty("taken", "false");}
                );
    }
    private void addHasWaiting( List<ArticleDTO> articles, Model model, String domainName ){
        Optional<WaitingDomainDTO> waitingDomainOptional = Optional.of(new WaitingDomainDTO());
        if( hasUser(model) ){
            waitingDomainOptional = ((UserDTO) model.getAttribute("user"))
                    .getWaitingDomains()
                    .stream().filter( w -> w.getNameDomain().equals(domainName))
                    .findFirst();
        }
        WaitingDomainDTO waitingDomain = new WaitingDomainDTO();
        if(waitingDomainOptional.isPresent()) waitingDomain = waitingDomainOptional.get();
        WaitingDomainDTO finalWaitingDomain = waitingDomain;
        articles.forEach(article ->{
            if(finalWaitingDomain.containTld( article ) ) article.addProperty("waiting","true");
            else article.addProperty("waiting", "false");
        });
    }

    private boolean hasUser(Model model){
        return model.containsAttribute("user") &&
                ((UserDTO) model.getAttribute("user")).isValid();
    }


    @GetMapping(value = "/product")
    public String redirect(){
        return "redirect:/product/domain";
    }

    @GetMapping(value = "/product/{category}")
    public String showByProduct(@PathVariable String category, Model model ){
        if( !category.equals("domain")
                && !category.equals("mail")
                && !category.equals("host")
         ) return "redirect:/product/domain";
        List<ArticleDTO> articles = articleService.getArticleDTOList( category );
        //-- translate
        articles.forEach(article -> translateService.translate(
                article ,
                (TranslateDTO) Objects.requireNonNull(model.getAttribute("langPage")))
        );
        model.addAttribute("articles" , articles );
        model.addAttribute("articleBuy", new ArticleDTO() );

        return "product/"+category;
    }

    @PostMapping("/product/domain")
    public String  domainCheck(
            @RequestParam( "domainSearch" ) String domainName,
            Model model ){
        // TODO if element is .Domain  return blanck
        domainName = domainName
                .toLowerCase()
                .split("\\.")[0]
                .replaceAll("[^a-z]","");

        // TODO need translate??
        List<ArticleDTO> articles = articleService.getArticleDTOList( "domain" );

        //----
        addDomainNameAndName( articles , domainName );
        addHasTaken( articles , domainName );
        addHasWaiting( articles , model , domainName );

        //----
        model.addAttribute("articles", articles);

        // Only need pas 1 new ArticleDTO
        model.addAttribute("articleBuy", new ArticleDTO() );
        model.addAttribute("articleWaiting", new ArticleDTO() );

        return "domaincheck";
    }

}
