package com.example.esquelet.controllers;

import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.entities.User;
import com.example.esquelet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RESTAuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (userService.checkUser(username, password)) {
            if (userService.isAdmin(username)) {
                return ResponseEntity.ok().body("Logged in as admin");
            } else {
                return ResponseEntity.ok().body("Users are not allowed to login");
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }

    }
}
