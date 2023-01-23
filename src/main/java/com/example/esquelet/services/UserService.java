package com.example.esquelet.services;

import com.example.esquelet.dtos.ServiceDTO;
import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.entities.User;
import com.example.esquelet.repositories.UserDataRepository;
import com.example.esquelet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDataRepository userDataRepository;

    public Optional<User> searchUser( UserDTO user ){
        return userRepository.findByUsername(user.getUsername());
    }

    public UserDTO getUser(UserDTO userDTO){
        User user = searchUser( userDTO ).get();
        UserDTO userResult= new UserDTO(user);
        userDataRepository.searchByUser(user).ifPresent(userResult::setUserData);
        return  userResult;
    }


    // boolean if you want newsletter
    public void addUser(UserDTO user) {
        // if(wantNewsLetter) newsLetterRepository.save(user.email)
        userRepository.save(user.getUserEntity());
    }

    public boolean checkUser(String userName, String password) {
        Optional<User> user = userRepository.findByUsername(userName);
        return user.map(value -> new BCryptPasswordEncoder().matches(password, value.getPassword())).orElse(false);
    }

    public boolean checkUser(String userName) {
        Optional<User> user = userRepository.findByUsername(userName);
        return user.isPresent();
    }

    public void addServices( UserDTO user ){
        userRepository.findByUsername( user.getUsername() ).ifPresent(
                userEntity -> userEntity.getServices()
                        .stream().map( ServiceDTO::createServiceDTO )
                        .forEach(user::addService)
        );
    }

    public void sendRegisterMail(UserDTO user) {
        String mail = user.getEmail();
        String subject = "Register";
        String text = "Welcome to ISP Hero";
        // TODO to send mail
    }

}
