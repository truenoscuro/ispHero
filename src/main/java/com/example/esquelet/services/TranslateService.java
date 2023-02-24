package com.example.esquelet.services;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.TranslateDTO;
import com.example.esquelet.entities.Lang;
import com.example.esquelet.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TranslateService {
    
    @Autowired
    private LanguageRepository languageRepository;

    public List<TranslateDTO> getAllSimple(){
        return languageRepository.findAll().stream().map(TranslateDTO::createSimpleTranslateDTO).toList();
    }

    public Lang language(String code){
        return languageRepository.findByCode( code ).get();
    }

    public void chargeMessages(TranslateDTO langPage){
        if(langPage.haveArticles()) return;
        langPage.chargeMessages( language( langPage.getCode() ) );
    }
    public void chargeArticles(TranslateDTO  langPage){
        if( langPage.haveArticles() ) return;
        langPage.chargeArticles(language(langPage.getCode()));
    }

    public void translate(ArticleDTO article , TranslateDTO translate ){
        chargeArticles(translate);
        if( !translate.haveArticles() ) return;
        Map<String,String> valueProperties = article.getProperty();
        String productName = translate.productEnglish(article.getProduct());
        // translate value properties
        if ( !translate.getValuePropertyByProduct().isEmpty() )
            translate.getValuePropertyByProduct()
                    .get( productName )
                    .forEach(valueProperties::replace);
        // translate product name;
        if(translate.getProduct().containsKey(productName))
            article.setProduct(translate.getProduct().get( productName ));
    }

}
