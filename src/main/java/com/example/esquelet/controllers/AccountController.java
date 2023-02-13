package com.example.esquelet.controllers;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.ServiceDTO;
import com.example.esquelet.dtos.UserDTO;

import com.example.esquelet.entities.Service;
import com.example.esquelet.models.Cart;
import com.example.esquelet.models.IdCart;
import com.example.esquelet.repositories.WaitingDomainRepository;
import com.example.esquelet.services.*;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage","articleComplete"})
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    ArticleService articleService;

    @Autowired
    WaitingDomainService waitingDomainService;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    ServService servService;
    @Autowired
    private WaitingDomainRepository waitingDomainRepository;


    @GetMapping("/account")
    public String account(Model model) {
        chargeUser( model );
        return "backendUser/account";
    }

    @PostMapping("/account/vincule")
    public String vincule( @RequestParam("idService") Long idService, Model model ){
        UserDTO user = (UserDTO) model.getAttribute("user");

        ServiceDTO service = user.getServices()
                .stream()
                .filter(serviceDTO-> Objects.equals(serviceDTO.getId(), idService) )
                .findFirst()
                .get();

        Long idCart = ((IdCart) model.getAttribute("articleComplete")).getId();

        ArticleDTO article = ((Cart) model.getAttribute("cartUser")).getArticle( idCart );

        // article <-- ServiceDTO
        article.getProperty().replace("needDomain","false");
        article.setService( service );

        return "redirect:/cart";
    }

    @GetMapping("/account/services/add")
    public String addService(Model model){
        chargeUser( model );
        return "backendUser/services";
    }
    private void chargeUser( Model model ){
        UserDTO userDTO =  userService.getUser((UserDTO) model.getAttribute("user"));
        servService.getServices( userDTO );
        invoiceService.getInvoices( userDTO );
        waitingDomainService.getWaitingDomainsByUser( userDTO );
        model.addAttribute("user",userDTO);
    }


    @GetMapping("/account/services")
    public String viewServices(Model model){
        servService.getServices( (UserDTO) Objects.requireNonNull( model.getAttribute("user" ) ) );
        return "backendUser/services";
    }

    @PostMapping("/account/waitingdomains")
    public String modifyWaitingDomain( @ModelAttribute ArticleDTO articleWaiting, Model model){
        waitingDomainService.addByUser(
                articleWaiting.getDomainName(),
                ( ( UserDTO ) model.getAttribute("user") ),
                articleWaiting.getProduct()
        );
        return "redirect:/account";
    }

    @PostMapping("/account/delete-waiting")
    public String deleteWaitingDomain( @ModelAttribute ArticleDTO articleWaiting, Model model){
        waitingDomainService.delete(
                articleWaiting.getDomainName(),
                ( ( UserDTO ) model.getAttribute("user") ),
                articleWaiting.getProduct()
        );
        return "redirect:/account";
    }
}
