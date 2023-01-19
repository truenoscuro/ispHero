package com.example.esquelet.controllers;

import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.repositories.LanguageControler;
import com.example.esquelet.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Objects;


@SessionAttributes(value = {"user","isLogged","cartUser"})
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    LanguageControler languageControler;

    // THis is use?

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("pageTitle", "Register");
        model.addAttribute("user",new UserDTO() );
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute UserDTO user , Model model, @RequestParam(defaultValue = "false") String status) {
        if (Objects.equals(status, "false")) {
            String username = user.getUsername();
            System.out.println("Username: " + username);
            // Check if any user has the same username
            if (userService.checkUser(username)) {
                model.addAttribute("error", "Username already exists");
                return "register";
            }
            user.setRole("USER");
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userService.addUser(user);
            model.addAttribute("user",user);
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
        model.addAttribute("languages",languageControler.findAll() );
        model.addAttribute("pageTitle", "Login");
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO user, Model model) {
        // Get user and password from form
        model.addAttribute("languages",languageControler.findAll() );
        String userName = user.getUsername();
        String password = user.getPassword();
        // Check user and password
        if (userService.checkUser(userName, password)) {
            model.addAttribute("user",userService.getUser( user ) );
            model.addAttribute("userName", userName);
            return "redirect:/account";
        } else {
            System.out.println("User or password incorrect");
            // Add error message
            model.addAttribute("case", "User or password incorrect");
            return "login";
        }
    }


    @GetMapping("/account")
    public String account(Model model) {
        model.addAttribute("languages",languageControler.findAll() );
        model.addAttribute("pageTitle", " My Account");
        model.addAttribute("isLogged", true);
        return "account";
    }
    
    @GetMapping("/logout")
    public String logout(Model model , SessionStatus status) {
        // TODO: Logout
        status.setComplete();
        return "redirect:/";
    }
}
