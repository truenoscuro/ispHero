package com.example.esquelet.controllers;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
public class OAuthController {


    @PostMapping("/auth/google")
    public ResponseEntity<String> googleAuth(Model model, @RequestBody String token) throws GeneralSecurityException, IOException {
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
        String firstName = (String) payload.get("given_name");
        String mail = payload.getEmail();

        // TODO: From here, you can get the user's email address, name, etc. and CREATE local token
        String localToken = "LOCALTOKEN OF "+ firstName + " WITH EMAIL " + mail;
        return new ResponseEntity<>(localToken, null, 200);
    }


    // Test purpose
   /* @GetMapping("/schedule")
    public ResponseEntity<List<String>> getSchedule (HttpRequest request) {
        String token = request.getHeaders().get("Authorization").toString();
        System.out.println(token);
        return null;
    }*/
}
