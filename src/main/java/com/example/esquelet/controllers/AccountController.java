package com.example.esquelet.controllers;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.UserDTO;

import com.example.esquelet.repositories.WaitingDomainRepository;
import com.example.esquelet.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage"})
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
