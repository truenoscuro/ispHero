package com.example.esquelet.controllers;

import com.example.esquelet.dtos.TranslateDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages"})

public class TranslateController {


}
