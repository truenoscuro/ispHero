package com.example.esquelet.controllers;


import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.DomainRegisteredDTO;
import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.dtos.WaitingDomainDTO;

import com.example.esquelet.services.ArticleService;
import com.example.esquelet.services.DomainRegisteredService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage"})
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private DomainRegisteredService domainRegisteredService;

    @GetMapping(value = "/product/{category}") // can pass product?
    public String showByProduct(@PathVariable String category, Model model ){

        model.addAttribute("articles",articleService.getArticleDTOList( category ));
        model.addAttribute("articleBuy",new ArticleDTO() );

        return "product/"+category;
    }

    // Pass ARTICLE ( Host or Email )
    @PostMapping("/domaincheck")
    public String  domainCheck(
            @RequestParam("domainSearch") String domainName,
            @RequestParam(name = "productName",required = false)  String productName,
            Model model ){

        if( productName!= null ) System.out.println( productName );


        List<ArticleDTO> articles = articleService.getArticleDTOList( "domain" );
        //----
        addDomainName(articles,domainName);
        addHasTaken(articles,domainName);
        addHasWaiting(articles,model,domainName);
        articles.forEach(System.out::println);
        //----
        model.addAttribute("articles", articles);
        model.addAttribute("articleBuy", new ArticleDTO());
        model.addAttribute("articleWaiting", new ArticleDTO());

        return "domaincheck";
        //return "redirect:/";
    }

    private void addDomainName(List<ArticleDTO> articles,String domainName){
        articles.forEach(article -> article.setDomainName(domainName));
        articles.forEach(article -> article.setName( domainName + article.getProperty().get("tld") ) );
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

}
