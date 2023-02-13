package com.example.esquelet.services;

import com.example.esquelet.dtos.*;
import com.example.esquelet.entities.*;
import com.example.esquelet.repositories.*;
import com.mailersend.sdk.Recipient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private TokenService tokenService;

    @Value("${MAIL_API_KEY}")
    private String MAIL_API_KEY;


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

    public boolean checkEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
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
        String tokenValidation = tokenService.createValidationToken(user.getEmail());
        registerMail.setHtml("<h1>ISP Hero</h1><p>Hi "+user.getUsername()+"</p><p>Thank you for registering with ISP Hero. Please click the link below to verify your email address.</p><p><a href=\"http://localhost:8080/validate/" + tokenValidation +  "\">Verify your email address</a></p><br><hr><br><p>This link will expire in 24 hours.</p><p>If you did not register with ISP Hero, please ignore this email.</p><p>Thanks for your interest in us,</p><p>ISP Hero</p>");

        MailerSend mailerSend = new MailerSend();
        mailerSend.setToken(MAIL_API_KEY);

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

    public User getUserByEmail( String mail ) {
        Optional<User> user = userRepository.findByEmail(mail);
        return user.orElse(null);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

}
