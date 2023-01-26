package com.example.esquelet.controllers;

import com.example.esquelet.config.LanguageConfig;

import com.example.esquelet.dtos.TranslateDTO;
import com.example.esquelet.repositories.LanguageRepository;
import com.example.esquelet.repositories.NewsLetterRepository;

import com.example.esquelet.services.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage"})

public class HomeController {

    @Autowired
    private TranslateService translateService;

    @GetMapping("/")
    public String index(Model model) {
        if(!model.containsAttribute("languages")){
            List<TranslateDTO> languages =  translateService.getAll();
            model.addAttribute("languages", translateService.getAll() );
            model.addAttribute("langPage",languages.get(0));
        }
        return "index";
    }


    @GetMapping("/about")
    public String about( Model model ) {
        return "about";
    }

    @GetMapping("/contact")
    public String contact( Model model ) {
        return "contact";
    }

}
