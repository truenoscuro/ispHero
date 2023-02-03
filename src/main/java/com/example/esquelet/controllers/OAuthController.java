package com.example.esquelet.controllers;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

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
        String userId = payload.getSubject();

        // TODO: From here, you can get the user's email address, name, etc. and CREATE local token
        String localToken = "LOCALTOKEN "+userId;
        return new ResponseEntity<>(localToken, null, 200);
    }
}
