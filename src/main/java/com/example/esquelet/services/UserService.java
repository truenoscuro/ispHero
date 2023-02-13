package com.example.esquelet.services;

import com.example.esquelet.dtos.*;
import com.example.esquelet.entities.*;
import com.example.esquelet.repositories.*;
import com.mailersend.sdk.Recipient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.exceptions.MailerSendException;


import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDataRepository userDataRepository;


    public Optional<User> searchUser( UserDTO user ){
        return userRepository.findByUsername(user.getUsername());
    }

    public UserDTO getUser( UserDTO userDTO ){
        User user = searchUser( userDTO ).get();
        UserDTO userResult = UserDTO.createUserDTO( user );
        userDataRepository.searchByUser( user ).ifPresent(userResult::setUserData);
        return  userResult;
    }

    public User getUser( String username ){
        return userRepository.findByUsername( username ).get();
    }


    // boolean if you want newsletter
    @Transactional
    public void addUser(UserDTO user) {
        // if(wantNewsLetter) newsLetterRepository.save(user.email)
        userRepository.save(user.getUserEntity());
    }

    public boolean checkUser( String userName , String password ) {
        Optional<User> user = userRepository.findByUsername(userName);
        return user.map(value -> new BCryptPasswordEncoder().matches(password, value.getPassword())).orElse(false);
    }

    public boolean checkUser( String userName ) {
        Optional<User> user = userRepository.findByUsername(userName);
        return user.isPresent();
    }

    public boolean isAdmin( String userName ) {
        Optional<User> user = userRepository.findByUsername(userName);
        return user.map(value -> value.getRole().equals(Role.ADMIN)).orElse(false);
    }


    public void sendRegisterMail( UserDTO user ) {
        Email registerMail = new Email();
        registerMail.setFrom("ISP Hero", "info@isphero.com");
        registerMail.setSubject("Please verify your address");

        Recipient recipient = new Recipient(user.getFirstName(), user.getEmail());

        registerMail.AddRecipient(recipient);

        registerMail.setHtml("<h1>ISP Hero</h1><p>Hi "+user.getUsername()+"</p><p>Thank you for registering with ISP Hero. Please click the link below to verify your email address.</p><p><a href=\"https://bitly.com/98K8eH"+"\">Verify your email address</a></p><br><hr><br><p>This link will expire in 24 hours.</p><p>If you did not register with ISP Hero, please ignore this email.</p><p>Thanks for your interest in us,</p><p>ISP Hero</p>");

        MailerSend mailerSend = new MailerSend();
        mailerSend.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiOGZhMzlmZTdlMmVkZTM0ODJkZDYyMDRkNzQxMmE1MDJlMmM4YWFlNWJkNmZjNmY0ZWQzY2E4NTY3YTFhYTk2ZmEyOGNjY2FiNTNkYjdjOWUiLCJpYXQiOjE2NzUyNDQ0MTguOTIzMSwibmJmIjoxNjc1MjQ0NDE4LjkyMzEwMywiZXhwIjo0ODMwOTE4MDE4LjkxNjc2Nywic3ViIjoiNTYwOTIiLCJzY29wZXMiOlsiZW1haWxfZnVsbCIsImRvbWFpbnNfZnVsbCIsImFjdGl2aXR5X2Z1bGwiLCJhbmFseXRpY3NfZnVsbCIsInRva2Vuc19mdWxsIiwid2ViaG9va3NfZnVsbCIsInRlbXBsYXRlc19mdWxsIiwic3VwcHJlc3Npb25zX2Z1bGwiLCJzbXNfZnVsbCIsImVtYWlsX3ZlcmlmaWNhdGlvbl9mdWxsIiwiaW5ib3VuZHNfZnVsbCIsInJlY2lwaWVudHNfZnVsbCIsInNlbmRlcl9pZGVudGl0eV9mdWxsIl19.fFXonujENY0bK7HoMPrTHWnH-eWBXwuTePS_DFp75ay010aus_zlony_FXhnB01KGx4XSI-GCEG9iJjuzIaPFivOMv62htYniRsZmKFyDFGLSGJmriC4Fzsl4K9MFO9TF_0m69DocCAGlwkE0SD3NWJS0VyQjqJh1oYZLdif4BzMQQYOy41WFMPSqu665IN120C0ZJU4dZdCW78nhbiM3D-lJl2sdDITflqiSyHcMscmg0R-sr8YD_vZsZ0ju81nH24i7Zx2iT7BtDHkfN9aduB7kO0YlTofT_zFGY6FntHs1Ub1axy6Tlg0Bg_UpoPL3Baf8qIuMlsXnvmPleRP3GKPNUJdRd-SkT4JD8voEtEO6eoNf6Xbz5R6_aSlkIFdNfXYDWfVRR-KzOtuAtQAZZ7JxzC_a1wSAFm9cYndl1C83ZkSrONio8m-uab3cgqmpZ-51ZtxgEfxoJyAMZeoxkNQPjONvGNXgDsmR2ktGeDrTzXgwUpUvEn9tHQkWuwUTzgQRBKGt1rkDiNhzQ9l0D4f0ZVROY6lCEP4yMQq6KWKBf32H3VovE9O3TFBJY9OYx17tnUykWCw81NYx0Z8sx68gZhE_GzBjZKi42aOifcy3k11JhKIF_svcl4dxQDvaUcqUblwlWpcZXSYM-hnyeSlShPqZUQS2W11_zDbw4I");

        try {
            MailerSendResponse response = mailerSend.emails().send(registerMail);
            System.out.println(response.messageId);
        } catch (MailerSendException e) {
            e.printStackTrace();
        }
    }


    public void addUserData(UserDTO user){
        userDataRepository.save(user.getUserDataEntity());
    }

    public User getUserByEmail(String mail) {
        Optional<User> user = userRepository.findByEmail(mail);
        return user.orElse(null);
    }
}
