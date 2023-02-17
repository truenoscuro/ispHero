package com.example.esquelet.services;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.TranslateDTO;
import com.example.esquelet.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TranslateService {

    @Autowired
    private LanguageRepository languageRepository;

    public List<TranslateDTO> getAll(){
        return languageRepository.findAll().stream().map(TranslateDTO::createTranslateDTO).toList();
    }

    public void translate(ArticleDTO article , TranslateDTO translate ){
        Map<String,String> valueProperties = article.getProperty();
        String productName = translate.productEnglish(article.getProduct());
        // translate value properties
        translate.getValuePropertyByProduct()
                .get( productName )
                .forEach(valueProperties::replace);
        // translate product name;
        if(translate.getProduct().containsKey(productName))
            article.setProduct(translate.getProduct().get( productName ));
    }



}
