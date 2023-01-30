package com.example.esquelet.controllers;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.services.UserService;

import com.example.esquelet.services.WaitingDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage"})

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WaitingDomainService waitingDomainService;

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("pageTitle","Register");
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute UserDTO user , Model model, @RequestParam(defaultValue = "false") String status) {
        if (Objects.equals(status, "false")) {
            String username = user.getUsername();
            // Check if any user has the same username
            if (userService.checkUser(username)) {
                model.addAttribute("error", "Username already exists");
                return "register";
            }
            user.setRole("USER");
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userService.addUser(user);
            model.addAttribute("user",userService.getUser(user));
            model.addAttribute("isLogged",true);
            model.addAttribute("status", "true");
        }
        else {
            userService.sendRegisterMail(user);
            model.addAttribute("status", "false");
        }
        return "register";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user",new UserDTO() );
        model.addAttribute("pageTitle", "Login");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("user",new UserDTO());
        model.addAttribute("isLogged",false);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO user, Model model) {
        // Get user and password from form
        String userName = user.getUsername();
        String password = user.getPassword();
        // Check user and password
        if (userService.checkUser(userName, password)) {
            model.addAttribute("isLogged",true);
            model.addAttribute("user",userService.getUser( user ) );
            model.addAttribute("userName", userName);
            model.addAttribute("isLogged", true);
            return "redirect:/";
        } else {
            System.out.println("User or password incorrect");
            // Add error message
            model.addAttribute("case", "User or password incorrect");
            return "login";
        }
    }

    @PostMapping("/message")
    public String messageSent() {
        return "messagesent";
    }

    @GetMapping("/password-recovery")
    public String passwordRecovery(Model model) {
        return "password-recovery";
    }

    @PostMapping("/password-recovery")
    public String passwordRecovery(@ModelAttribute UserDTO user, Model model) {
        if (userService.checkUser(user.getUsername())) {
            // TODO: userService.sendPasswordRecoveryMail(user);
            model.addAttribute("status", "true");
        } else {
            model.addAttribute("status", "false");
        }
        return "messagesent";
    }
}
