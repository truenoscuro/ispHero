package com.example.esquelet.services;

import com.example.esquelet.entities.User;
import com.example.esquelet.entities.UserData;
import com.example.esquelet.repositories.UserDataRepository;
import com.example.esquelet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
