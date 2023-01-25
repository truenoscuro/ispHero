package com.example.esquelet.services;

import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.dtos.WaitingDomainDTO;
import com.example.esquelet.entities.User;
import com.example.esquelet.entities.WaitingDomain;
import com.example.esquelet.repositories.UserRepository;
import com.example.esquelet.repositories.WaitingDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaitingDomainService {

    @Autowired
    WaitingDomainRepository waitingDomainRepository;

    @Autowired
    UserRepository userRepository;
    public List<WaitingDomainDTO> getAllByUser(UserDTO user){
        User userEntity = userRepository.findByUsername(user.getUsername()).get();
        return  waitingDomainRepository.findAllByUser(userEntity)
                .stream().collect(Collectors.groupingBy(WaitingDomain::getNameDomain))
                .values()
                .stream().map(WaitingDomainDTO::createWaitingDomainDTO)
                .toList();
    }
}
