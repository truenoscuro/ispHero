package com.example.esquelet.controllers;


import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.DomainRegisteredDTO;
import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.dtos.WaitingDomainDTO;
import com.example.esquelet.entities.WaitingDomain;
import com.example.esquelet.services.ArticleService;
import com.example.esquelet.services.DomainRegisteredService;
import com.example.esquelet.services.WaitingDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage"})
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private DomainRegisteredService domainRegisteredService;

    @Autowired
    private WaitingDomainService waitingDomainService;

    @GetMapping(value = "/product/{category}") // can pass product?
    public String showByProduct(@PathVariable String category, Model model ){;
        model.addAttribute("articles",articleService.getArticleDTOList( category ));
        model.addAttribute("articleBuy",new ArticleDTO() );
        return "product/"+category;
    }

    @PostMapping("/domaincheck")
    public String  domainCheck(@RequestParam("domainSearch") String domainName, Model model ){
        List<ArticleDTO> articles = articleService.getArticleDTOList( "domain" );
        DomainRegisteredDTO domainRegistered = domainRegisteredService.getDomainRegisteredDTO(domainName);
        model.addAttribute("domainName" , domainName );
        model.addAttribute("hasUser",hasUser(model));
        Optional<WaitingDomainDTO> waitingDomain = Optional.of(new WaitingDomainDTO());
        if( hasUser(model) ){
            waitingDomain =  waitingDomainService.getAllByUser((UserDTO) model.getAttribute("user"))
                    .stream().filter(waitingDomainDTO -> waitingDomainDTO.getNameDomain().equals(domainName))
                    .findAny();

        }
        WaitingDomainDTO finalWaitingDomain = new WaitingDomainDTO();
        if(waitingDomain.isPresent()) finalWaitingDomain = waitingDomain.get();
        WaitingDomainDTO finalWaitingDomain1 = finalWaitingDomain;
        model.addAttribute(
                "articles",
                articles.stream()
                        .peek(article ->{
                            if (domainRegistered.containTld(article)) article.addProperty("taken", "true");
                            else article.addProperty("taken", "false");})
                        .peek(article ->{
                            if(finalWaitingDomain1.containTld(article) ) article.addProperty("waiting","true");
                            else article.addProperty("waiting", "false");})
                        .toList()
        );

        return "domaincheck";
    }




    private boolean hasUser(Model model){
        return model.containsAttribute("user") &&
                ((UserDTO) model.getAttribute("user")).isValid();
    }


}
