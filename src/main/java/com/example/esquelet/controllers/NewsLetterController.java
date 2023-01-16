package com.example.esquelet.controllers;


import com.example.esquelet.entities.NewsLetter;
import com.example.esquelet.repositories.NewsLetterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsLetterController {

    @Autowired
    NewsLetterRepository newsLetterRepository;

    @PostMapping("/subscribe")
    public String newsletter(@RequestParam String email) {
        // Check if email is already in the database
        if (!newsLetterRepository.existsByEmail(email)) {
            NewsLetter newsLetter = new NewsLetter();
            newsLetter.setEmail(email);
            newsLetterRepository.save(newsLetter);
            return "200";
        } else {
            return "201";
        }
    }
}
