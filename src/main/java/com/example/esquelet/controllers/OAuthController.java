package com.example.esquelet.controllers;


import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.entities.Role;
import com.example.esquelet.entities.User;
import com.example.esquelet.repositories.UserRepository;
import com.example.esquelet.services.TokenService;
import com.example.esquelet.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage","articleComplete"})
@RestController
public class OAuthController {

    @Autowired
    TokenService tokenService;
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/auth/google")
    public ResponseEntity<String> googleAuth( @RequestBody String token ) throws GeneralSecurityException, IOException {
        final NetHttpTransport TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleIdTokenVerifier tokenVerifier = new GoogleIdTokenVerifier.Builder(
                TRANSPORT,
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList("578391080478-tld06kdi3jv6guggqbuj5vrua8cq15vh.apps.googleusercontent.com"))
                .build();
        GoogleIdToken idToken = tokenVerifier.verify(token);
        if (idToken == null) {
            return new ResponseEntity<>("Unauthorized", null, 401);
        }
        GoogleIdToken.Payload payload = idToken.getPayload();
        String mail = payload.getEmail();
        System.out.println(mail);

        User user = userService.getUserByEmail(mail);

        if (user == null) {
            return new ResponseEntity<>("Wrong credentials", null, 401);
        }
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", null, 401);
        }

        String tokenResponse = tokenService.createToken(user.getEmail());

        return new ResponseEntity<>(tokenResponse, null, 200);
    }

    @PostMapping("/auth/verify")
    public ResponseEntity<String> verifyToken(@RequestBody String token) {
        String tokenToValidate;
        tokenToValidate = token.substring(10, token.length() - 2);
        if (tokenService.validateToken(tokenToValidate) == 0) {
            return new ResponseEntity<>("valid", null, 200);
        } else {
            return new ResponseEntity<>("false", null, 401);
        }
    }

    @PostMapping("/auth/login/google")
    public ResponseEntity<String> googleAuthLogin( @RequestBody String token, Model model) throws GeneralSecurityException, IOException {
        final NetHttpTransport TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleIdTokenVerifier tokenVerifier = new GoogleIdTokenVerifier.Builder(
                TRANSPORT,
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList("578391080478-tld06kdi3jv6guggqbuj5vrua8cq15vh.apps.googleusercontent.com"))
                .build();
        GoogleIdToken idToken = tokenVerifier.verify(token);
        if (idToken == null) {
            return new ResponseEntity<>("Unauthorized", null, 401);
        }
        GoogleIdToken.Payload payload = idToken.getPayload();
        String mail = payload.getEmail();
        System.out.println(mail);
        User user = userService.getUserByEmail(mail);

        if (user == null) {
            user = new User();
            user.setEmail(mail);
            user.setRole(Role.USER);
            user.setVerified(true);
            userRepository.save(user);
        }

        System.out.println("Login of user: " + user.getEmail());
        token = tokenService.createToken(user.getEmail());
        return new ResponseEntity<>(token, null, 200);
    }

}
