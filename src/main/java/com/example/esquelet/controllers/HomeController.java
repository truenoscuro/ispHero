package com.example.esquelet.controllers;

import com.example.esquelet.config.LanguageConfig;
import com.example.esquelet.entities.NewsLetter;
import com.example.esquelet.repositories.LanguageControler;
import com.example.esquelet.repositories.NewsLetterRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

@Controller
public class HomeController {

    @Autowired
    LanguageControler languageControler;
    @Autowired
    NewsLetterRepository newsLetterRepository;
    @Autowired
    LanguageConfig languageConfig;

    @GetMapping("/")
    public String index(Model model, HttpSession session, HttpServletRequest request) {
        model.addAttribute("languages",languageControler.findAll() );
        System.out.println("Session: " + session.getAttribute("user"));
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
