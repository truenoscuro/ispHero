package com.example.esquelet.controllers;

import com.example.esquelet.config.LanguageConfig;

import com.example.esquelet.repositories.LanguageControler;
import com.example.esquelet.repositories.NewsLetterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser"})
public class HomeController {

    @Autowired
    LanguageControler languageControler;
    @Autowired
    NewsLetterRepository newsLetterRepository;
    @Autowired
    LanguageConfig languageConfig;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("languages",languageControler.findAll() );
        if (model.containsAttribute("user")) {
            model.addAttribute("isLogged", true); }
        return "index";
    }


    @GetMapping("/about")

    public String about( Model model ) {
        model.addAttribute("languages",languageControler.findAll() );

        return "about";
    }

    @GetMapping("/contact")
    public String contact( Model model ) {
        model.addAttribute("languages",languageControler.findAll() );
        return "contact";
    }

}
