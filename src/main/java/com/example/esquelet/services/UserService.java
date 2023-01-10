package com.example.esquelet.services;

import com.example.esquelet.entities.User;
import com.example.esquelet.entities.UserData;
import com.example.esquelet.repositories.UserDataRepository;
import com.example.esquelet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDataRepository userDataRepository;

    public Optional<User> searchUser( User user ){
        return userRepository.searchUserByUsernameEquals(user.getUsername());
    }

    public boolean canAddUser(UserData userData){
        return ! userDataRepository.existsByUser( userData.getUser( ) );
    }


    public void addUser(UserData userData){
        User user = userData.getUser();
        userRepository.save(user); // encript passw
        userDataRepository.save(userData);
    }

    // boolean if you want newsletter
    public void addUser(User user) {
        // if(wantNewsLetter) newsLetterRepository.save(user.email)

        userRepository.save(user);
    }

    public boolean checkUser(String userName, String password) {
        Optional<User> user = userRepository.searchUserByUsernameEquals(userName);
        return user.map(value -> value.getPassword().equals(password)).orElse(false);
    }

    public boolean checkUser(String userName) {
        Optional<User> user = userRepository.searchUserByUsernameEquals(userName);
        return user.isPresent();
    }

    public void sendRegisterMail(User user) {
        String mail = user.getEmail();
        String subject = "Register";
        String text = "Welcome to ISP Hero";
        // TODO to send mail
    }

}
