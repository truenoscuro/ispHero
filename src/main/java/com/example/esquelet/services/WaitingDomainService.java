package com.example.esquelet.services;

import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.dtos.WaitingDomainDTO;
import com.example.esquelet.entities.Article;
import com.example.esquelet.entities.User;
import com.example.esquelet.entities.WaitingDomain;
import com.example.esquelet.repositories.ArticleRepository;
import com.example.esquelet.repositories.ProductRepository;
import com.example.esquelet.repositories.UserRepository;
import com.example.esquelet.repositories.WaitingDomainRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WaitingDomainService {

    @Autowired
    WaitingDomainRepository waitingDomainRepository;

    @Autowired
    UserRepository user;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;


    public void getWaitingDomainsByUser(UserDTO user){
        User userEntity = userRepository.findByUsername(user.getUsername()).get();
        waitingDomainRepository.findAllByUser(userEntity)
                .stream().collect(Collectors.groupingBy(WaitingDomain::getNameDomain))
                .values()
                .stream().map(WaitingDomainDTO::createWaitingDomainDTO)
                .forEach(user::addWaitingDomain);
    }


    @Transactional
    public void addByUser(String domainName, UserDTO userDTO, String productName){

        User user = userRepository.findByUsername(userDTO.getUsername()).get();

        Article tld = productRepository.getProductsByName(productName).get()
                .getArticles()
                .stream().filter(a -> a.getProperty().getName().equals("tld"))
                .findFirst()
                .get();

        waitingDomainRepository.save( new WaitingDomain( domainName , user , tld ) );

    }

}
