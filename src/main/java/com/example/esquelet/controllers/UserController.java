package com.example.esquelet.controllers;

import com.example.esquelet.entities.Role;
import com.example.esquelet.entities.User;
import com.example.esquelet.entities.UserData;
import com.example.esquelet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
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
        return "account"; //create page
    }


    @PostMapping("/register")
    public String addUser(@ModelAttribute("user") User user , Model model, @RequestParam(defaultValue = "false") String status) {

        if (Objects.equals(status, "false")) {
            String username = user.getUsername();
            System.out.println("Username: " + username);
            // Check if any user has the same username
            if (userService.checkUser(username)) {
                model.addAttribute("error", "Username already exists");
                return "register";
            }
            user.setRole(Role.USER);
            userService.addUser(user);
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
        model.addAttribute("pageTitle", "Login");
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model) {
        // Get user and password from form
        String userName = user.getUsername();
        String password = user.getPassword();
        System.out.println("User: " + userName + " Password: " + password);
        // Check user and password
        if (userService.checkUser(userName, password)) {
            model.addAttribute("auth", userName);
            System.out.println(userName + " logged in");
//            System.out.println(user.getUserData().getFirstName()); // need to get user data !!
            return "account";
        } else {
            System.out.println("User or password incorrect");
            // Add error message
            model.addAttribute("case", "User or password incorrect");
            return "login";
        }
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("pageTitle", "Register");
        return "register";
    }

    @GetMapping("/account")
    public String account(Model model) {
        if (model.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("pageTitle", " My Account");
        return "account";
    }
}
