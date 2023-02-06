package com.example.esquelet.services;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.DomainRegisteredDTO;
import com.example.esquelet.entities.Article;
import com.example.esquelet.entities.DomainRegistered;
import com.example.esquelet.models.Cart;
import com.example.esquelet.repositories.ArticleRepository;
import com.example.esquelet.repositories.DomainRegisteredRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class DomainRegisteredService {

    @Autowired
    private DomainRegisteredRepository domainRegisteredRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public DomainRegisteredDTO getDomainRegisteredDTO(String nameDomain) {
        return domainRegisteredRepository.searchByName(nameDomain)
                .map(DomainRegisteredDTO::createDomainRegisteredDTO)
                .orElseGet(DomainRegisteredDTO::new);
    }

    @Transactional
    public void updateDomainRegisteredWithCart(Cart cart){
        cart.getArticles().stream()
                .filter( article -> !Objects.equals(article.getDomainName(), ""))
                .forEach( article ->{
                    DomainRegistered domain = new DomainRegistered(article.getName());
                    Optional<DomainRegistered> domainOptional = domainRegisteredRepository.searchByName(domain.getName());
                    if( domainOptional.isPresent() ){
                        domain =  domainOptional.get();
                    } else {
                        domain = domainRegisteredRepository.save( domain );
                        domainRegisteredRepository.flush();
                    }
                    Article a = articleRepository.searchArticleByValueProperty( article.getProperty().get("tld") ).get();
                    a.getDomains().add(domain);
                    articleRepository.save(a);
                });

    }


}
