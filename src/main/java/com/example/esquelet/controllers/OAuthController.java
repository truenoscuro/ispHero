package com.example.esquelet.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {


    @PostMapping("/auth/google")
    public ResponseEntity<String> googleAuth(Model model, @RequestBody String token) {

        return new ResponseEntity<>("OK, Logged In", null, 200);
    }
}
