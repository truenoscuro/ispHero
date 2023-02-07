package com.example.esquelet.services;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.ServiceDTO;
import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.entities.Article;
import com.example.esquelet.entities.User;
import com.example.esquelet.models.Cart;
import com.example.esquelet.repositories.ProductRepository;
import com.example.esquelet.repositories.ServiceRepository;
import com.example.esquelet.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public void getServices( UserDTO user ){
        userRepository.findByUsername( user.getUsername() )
                .ifPresent( userEntity -> userEntity.getServices().stream()
                        .map( ServiceDTO::createServiceDTO )
                        .forEach( user::addService )
                );
    }

    @Transactional
    public  void addServicesByUser(UserDTO userDTO, Cart cart){

        User user = userRepository.findByUsername(userDTO.getUsername()).get();
        List<ArticleDTO> articles = cart.getArticles();

        // Service Save
        List<Article> articlesEntity = articles.stream()
                .map( a -> productRepository.getProductsByName(a.getProduct())
                        .get().getArticles()
                        .stream().findFirst()
                        .get()
                ).toList();

        for( int i = 0 ; i < articlesEntity.size() ; i++ )
            serviceRepository.save(
                    addService( articles.get(i).getName(),
                            articlesEntity.get(i),
                            user
                    )
            );

    }
    private com.example.esquelet.entities.Service addService(String nameDomain , Article article, User user){
        com.example.esquelet.entities.Service service = new com.example.esquelet.entities.Service();
        service.setArticle(article);
        service.setUser( user );
        service.setDateExpired(LocalDateTime.now().plusYears(1));
        service.setCancelled( false );
        if(nameDomain != null) service.setNameDomain(nameDomain);
        return  service;
    }


}
