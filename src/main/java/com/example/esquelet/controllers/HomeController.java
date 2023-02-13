package com.example.esquelet.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage"})
public class HomeController {

    @GetMapping("/")
    public String index(Model model) { return "home/index"; }

    @GetMapping("/about")
    public String about( Model model ) {
        return "home/about";
    }

    @GetMapping("/contact")
    public String contact( Model model ) {
        return "home/contact";
    }

    @GetMapping("/terms-of-service")
    public String termsOfService( Model model ) {
        return "home/terms_of_service";
    }

    @GetMapping("/privacy-policy")
    public String privacyPolicy( Model model ) {
        return "home/privacy_policy";
    }

    @GetMapping("/cookies-policy")
    public String cookiesPolicy( Model model ) {
        return "home/cookies";
    }
}
