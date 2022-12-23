package com.example.esquelet.controller;

import ch.qos.logback.core.model.Model;
import com.example.esquelet.models.User;
import com.example.esquelet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;
    @PostMapping("/user") // Change name form name
    public String userAcces(@ModelAttribute("user") User user , Model model){
        Optional<User> userOptional = userRepository.searchUserByUsernameEquals(user.getUsername());
        if(userOptional.isEmpty()) return "index"; // name fail
        User userDataBase = userOptional.get();
        if(!userDataBase.getPassword().equals(user.getPassword())) return  "index"; // password fail
        // user and password done!
        return "privatePage"; //create page
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("user") User user , Model model){

        
        return "index";
    }

}
