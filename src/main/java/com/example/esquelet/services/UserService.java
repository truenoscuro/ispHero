package com.example.esquelet.services;

import com.example.esquelet.dtos.InvoiceDTO;
import com.example.esquelet.dtos.ServiceDTO;
import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.dtos.WaitingDomainDTO;
import com.example.esquelet.entities.User;
import com.example.esquelet.entities.WaitingDomain;
import com.example.esquelet.repositories.UserDataRepository;
import com.example.esquelet.repositories.UserRepository;
import com.example.esquelet.repositories.WaitingDomainRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private WaitingDomainRepository waitingDomainRepository;



    public Optional<User> searchUser( UserDTO user ){
        return userRepository.findByUsername(user.getUsername());
    }

    public UserDTO getUser( UserDTO userDTO ){
        User user = searchUser( userDTO ).get();
        UserDTO userResult = UserDTO.createUserDTO( user );
        userDataRepository.searchByUser( user ).ifPresent(userResult::setUserData);
        return  userResult;
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

    public void getServices( UserDTO user ){
        userRepository.findByUsername( user.getUsername() )
                .ifPresent( userEntity -> userEntity.getServices().stream()
                        .map( ServiceDTO::createServiceDTO )
                        .forEach( user::addService )
                );
    }

    public void getInvoices( UserDTO user ){
        userRepository.findByUsername( user.getUsername() )
                .ifPresent( u -> u.getInvoices().stream()
                        .map( InvoiceDTO::createInvoiceDTO )
                        .forEach( user::addInvoice )
                );
    }
    public void getWaitingDomains(UserDTO user){
        User userEntity = userRepository.findByUsername(user.getUsername()).get();
        waitingDomainRepository.findAllByUser(userEntity)
                .stream().collect(Collectors.groupingBy(WaitingDomain::getNameDomain))
                .values()
                .stream().map(WaitingDomainDTO::createWaitingDomainDTO)
                .forEach(user::addWaitingDomain);
    }




    public void sendRegisterMail( UserDTO user ) {
        String mail = user.getEmail();
        String subject = "Register";
        String text = "Welcome to ISP Hero";

    }


    public void addUserData(UserDTO user){
        userDataRepository.save(user.getUserDataEntity());
    }

}
