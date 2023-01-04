package com.example.esquelet.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Index");
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "About Us");
        return "about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("pageTitle", "Contact Us");
        return "contact";
    }

    @PostMapping("/message")
    public String message(Model model, @RequestParam String name, @RequestParam String email, @RequestParam String message) {
        // TODO Send email to admin with name, message and email
        model.addAttribute("pageTitle", "Contact Us");
        return "messagesent";
    }

}
