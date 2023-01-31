package com.example.esquelet.controllers;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.entities.Article;
import com.example.esquelet.entities.User;
import com.example.esquelet.services.ArticleService;
import com.example.esquelet.services.UserService;
import com.example.esquelet.services.WaitingDomainService;
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


    @GetMapping("/account")
    public String account(Model model) {
        model.addAttribute("pageTitle", " My Account");
        model.addAttribute("isLogged", true);
       // model.addAttribute("waitingDomains",waitingDomainService.getAllByUser( (UserDTO) model.getAttribute("user") ) );
        model.addAttribute("userData",new UserDTO());
        chargeUser( model );
        return "backendUser/account";
    }

    public void chargeUser( Model model ){
        UserDTO userDTO =  userService.getUser((UserDTO) model.getAttribute("user"));
        userService.getServices( userDTO );
        userService.getInvoices( userDTO );
        userService.getWaitingDomains( userDTO );
        model.addAttribute("user",userDTO);
    }


    @GetMapping("/account/services")
    public String viewServices(Model model){
        userService.getServices( (UserDTO) Objects.requireNonNull( model.getAttribute("user" ) ) );
        return "backendUser/services";
    }

    @PostMapping("/account/waitingdomains")
    public String modifyWaitingDomain( @ModelAttribute ArticleDTO articleWaiting, Model model){
        waitingDomainService.update(
                articleWaiting.getDomainName(),
                ( ( UserDTO ) model.getAttribute("user") ),
                articleWaiting.getProduct()
        );
        return "redirect:/account/waitingdomains";
    }


    @GetMapping("/account/userdata")
    public String formUSerData(Model model){
        model.addAttribute("userData",new UserDTO());
        return "backendUser/userdata";
    }

    @PostMapping("/account/update")
    public String updateUserData(@ModelAttribute UserDTO userData , Model model){
        UserDTO user = (UserDTO) model.getAttribute("user");
        user.setUserData(userData);
        userService.addUserData(user);
        model.addAttribute("user",userService.getUser(user) );

        System.out.println(user);
        return "redirect:/account";
    }

}
