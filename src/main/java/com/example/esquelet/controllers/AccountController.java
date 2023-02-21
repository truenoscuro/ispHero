package com.example.esquelet.controllers;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.ServiceDTO;
import com.example.esquelet.dtos.UserDTO;

import com.example.esquelet.models.Cart;
import com.example.esquelet.models.IdCart;
import com.example.esquelet.repositories.WaitingDomainRepository;
import com.example.esquelet.services.*;

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

    private void chargeUser( Model model ){
        UserDTO userDTO =  userService.getUser((UserDTO) model.getAttribute("user"));
        servService.getServices( userDTO );
        invoiceService.getInvoices( userDTO );
        waitingDomainService.getWaitingDomainsByUser( userDTO );
        model.addAttribute("user",userDTO);
    }

    @GetMapping("/account")
    public String account( Model model ) {
        chargeUser( model );
        return "backendUser/account";
    }

    @GetMapping(value = "/account/{page}/{action}")
    public String redirect(@PathVariable String page,
                           @PathVariable(required = false) String action ){
        if(page.equals("services")
                && action != null
                && action.equals("add") )
            return "redirect:/account/services";
        return "redirect:/account" ;

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

        article.getProperty().replace("needDomain","false");
        article.setService( service );

        return "redirect:/cart";
    }

    @GetMapping("/account/services")
    public String addService(Model model){
        if(model.containsAttribute("articleComplete")
                && ((IdCart) model.getAttribute("articleComplete")).getId() != null
        ) {
            chargeUser(model);
            return "backendUser/services";
        }
        return "redirect:/account";
    }
    @PostMapping("account/service/expire")
    public String updateService(@RequestParam Long idService){
        servService.changeExpired( idService );
        return "redirect:/account";
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

    @PostMapping("/account/update")
    public String updateUserData(@ModelAttribute UserDTO userData , Model model){
        UserDTO user = (UserDTO) model.getAttribute("user");
        user.setUserData(userData);
        userService.addUserData(user);
        return "redirect:/account";
    }

}
