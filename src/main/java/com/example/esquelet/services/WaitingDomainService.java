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

import java.util.List;
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

    public List<WaitingDomainDTO> getAllByUser(UserDTO user){
        User userEntity = userRepository.findByUsername(user.getUsername()).get();
        return  waitingDomainRepository.findAllByUser(userEntity)
                .stream().collect(Collectors.groupingBy(WaitingDomain::getNameDomain))
                .values()
                .stream().map(WaitingDomainDTO::createWaitingDomainDTO)
                .toList();
    }

    @Transactional
    public void update(String domainName,UserDTO userDTO, String productName){

        User user = userRepository.findByUsername(userDTO.getUsername()).get();

        Article tld = productRepository.getProductsByName(productName).get()
                .getArticles()
                .stream().filter(a -> a.getProperty().getName().equals("tld"))
                .findFirst()
                .get();
        //save

        Optional<WaitingDomain> waitingDomain = waitingDomainRepository.findWaitingDomainByUserAndTld(user,tld);
        if(waitingDomain.isPresent()) waitingDomainRepository.delete( waitingDomain.get() );
        else waitingDomainRepository.save( new WaitingDomain(domainName,user,tld) );

    }

    @Transactional
    public void delete(String domainName, UserDTO userDTO, String productName) {
        User user = userRepository.findByUsername(userDTO.getUsername()).get();

        Article tld = productRepository.getProductsByName(productName).get()
                .getArticles()
                .stream().filter(a -> a.getProperty().getName().equals("tld"))
                .findFirst()
                .get();

        Optional<WaitingDomain> waitingDomain = waitingDomainRepository.findWaitingDomainByUserAndTld(user,tld);
        waitingDomain.ifPresent(domain -> waitingDomainRepository.delete(domain));
    }
}
