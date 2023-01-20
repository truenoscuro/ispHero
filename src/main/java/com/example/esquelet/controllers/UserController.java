package com.example.esquelet.controllers;

import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.entities.UserData;
import com.example.esquelet.repositories.LanguageRepository;
import com.example.esquelet.repositories.UserDataRepository;
import com.example.esquelet.repositories.UserRepository;
import com.example.esquelet.services.TranslateService;
import com.example.esquelet.services.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Objects;


@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage"})

@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDataRepository userDataRepository;

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
        model.addAttribute("pageTitle", "Login");
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO user, Model model) {
        // Get user and password from form
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

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("pageTitle", "Register");
        return "register";
    }

    /*@GetMapping("/account")
    public String account(Model model) {
        model.addAttribute("pageTitle", " My Account");
        model.addAttribute("isLogged", true);
        model.addAttribute("user", userRepository.findByUsername((String) session.getAttribute("user")));

        System.out.println("User: " + userRepository.findByUsername((String) session.getAttribute("user")));
        // Get the userData associated to this user id
        UserData userData = userDataRepository.findByUser((User) userRepository.findByUsername((String) session.getAttribute("user")));
        System.out.println("UserData: " + userData);
        model.addAttribute("userData", userData);
        return "account";
    }*/

    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("user",null);
        model.addAttribute("isLogged",false);
        return "redirect:/";
    }
}
