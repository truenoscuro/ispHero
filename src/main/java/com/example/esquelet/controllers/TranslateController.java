package com.example.esquelet.controllers;

import com.example.esquelet.dtos.TranslateDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage"})

public class TranslateController {

    @GetMapping(value = "translate/{code}")
    public String translate(@PathVariable String code, Model model){
        List<TranslateDTO> languages = (List<TranslateDTO>) model.getAttribute("languages");
        model.addAttribute("langPage"
                ,languages.stream()
                .filter( language -> language.getCode().equals(code))
                .findFirst().get());
        return "redirect:/?lang=" + code;
    }
}
