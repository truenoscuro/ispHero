package com.example.esquelet.controller;

import ch.qos.logback.core.model.Model;
import com.example.esquelet.models.User;
import com.example.esquelet.models.UserData;
import com.example.esquelet.repositories.UserRepository;
import com.example.esquelet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user") // Change name form name
    public String userAcces(@ModelAttribute("user") User user , Model model){
        Optional<User> userOptional = userService.searchUser(user);
        if(userOptional.isEmpty()) return "index"; // name fail
        User userDataBase = userOptional.get(); // need passwordEncrypt
        if(!userDataBase.getPassword().equals(user.getPassword())) return  "index"; // password fail
        // user and password done!
        return "privatePage"; //create page
    }


    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("userData") UserData userData , Model model){
        // multi-email accept or multi-count accept?
        if(! userService.canAddUser( userData ) ) return "index";
        userService.addUser(userData); // encryptPassw
        return "succesAdd";
    }

}
