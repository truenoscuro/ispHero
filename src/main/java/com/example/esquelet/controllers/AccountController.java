package com.example.esquelet.controllers;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.ServiceDTO;
import com.example.esquelet.dtos.UserDTO;

import com.example.esquelet.models.Cart;
import com.example.esquelet.models.IdCart;
import com.example.esquelet.repositories.WaitingDomainRepository;
import com.example.esquelet.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage","articleComplete"})
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private WaitingDomainService waitingDomainService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ServService servService;

    @Autowired
    private ProfileImgService profileImgService;



    private void chargeUser( Model model ){
        UserDTO userDTO =  userService.getUser((UserDTO) model.getAttribute("user"));
        profileImgService.getProfileImg( userDTO );
        servService.getServices( userDTO );
        invoiceService.getInvoices( userDTO );
        waitingDomainService.getWaitingDomainsByUser( userDTO );

        model.addAttribute("user",userDTO);
    }

    @GetMapping("/account")
    public String account( Model model ) {
        chargeUser( model );
        model.addAttribute("userPassword" , new UserDTO() );
        return "backendUser/account";
    }

    @GetMapping(value = "/account/{page}/{action}")
    public String redirect(@PathVariable String page,
                           @PathVariable(required = false) String action ){
        if(!page.equals("services") || action == null) return "redirect:/account";
        if(action.equals("add")) return "redirect:/account/services";
        if(Integer.parseInt(action) >= 0) return "redirect:/account/service/expire/{idService}";
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
    @GetMapping("account/service/expire/{id}")
    public String updateService(@PathVariable("id") Long idService){
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

    @PostMapping("/account/changePassword")
    public String updatePassword(@ModelAttribute UserDTO userPassword, Model model){
        UserDTO user = (UserDTO) model.getAttribute("user");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if( !bCryptPasswordEncoder.matches( userPassword.getPassword() ,  user.getPassword() ) ){
            // TODO need confirm to fail
            // password original diff
            System.out.println("password original diff");
            return "redirect:/account";
        }
        if( !userPassword.getPassword2().equals( userPassword.getPasswordRepeat( ) ) ){
            // password diff
            System.out.println("new passwords diff");
            return "redirect:/account";
        }
        String newPassword = bCryptPasswordEncoder.encode( userPassword.getPassword2( ) );
        userService.updatePassword( user , newPassword );
        return "redirect:/account";
    }
    @GetMapping("/account/updateProfileImg")
    public String updateProfileImg( Model model){
        return "img";
    }


    @PostMapping("/account/updateProfileImg")
    public String updateProfileImg(@RequestParam("image") MultipartFile imageFile, Model model) throws IOException {
        profileImgService.saveProfileImg(
                (UserDTO) Objects.requireNonNull(model.getAttribute("user")),
                imageFile
        );
        return "redirect:/account";
    }

}
