package com.example.esquelet.controllers;

import com.example.esquelet.entities.User;
import com.example.esquelet.entities.UserData;
import com.example.esquelet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user") // Change name form name
    public String userAcces(@ModelAttribute("user") User user , Model model){
        Optional<User> userOptional = userService.searchUser(user);
        if(userOptional.isEmpty()) return "index"; // name fail
        User userDataBase = userOptional.get(); // need passwordEncrypt
        if(!userDataBase.getPassword().equals(user.getPassword())) return  "index"; // password fail
        // user and password done!
        return "privatePage"; //create page
    }


    @PostMapping("/register")
    public String addUser(@ModelAttribute("userData") UserData userData , Model model){
        // multi-email accept or multi-count accept?
        if(! userService.canAddUser( userData ) ) return "index";
        userService.addUser(userData); // encryptPassw
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Login");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("pageTitle", "Register");
        return "register";
    }
}
