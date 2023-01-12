package com.example.esquelet.controllers;

import com.example.esquelet.repositories.LanguageControler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class HomeController {

    @Autowired
    LanguageControler languageControler;


    @GetMapping("/")
    public String index( Model model) {
        model.addAttribute("languages",languageControler.findAll() );

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
