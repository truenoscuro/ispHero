package com.example.esquelet.dtos;

import com.example.esquelet.entities.Lang;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TranslateDTO {
    // Lang
    private String code;
    private String name;

    private Map<String,String> product;
    private Map<String,String> property;
    private Map<String,String> messages;

    private Map<String,Map<String,String>> valuePropertyByProduct;

    public TranslateDTO( String code , String name ){
        this.code = code;
        this.name = name;
    }

    public boolean haveMessages(){ return messages != null && !messages.isEmpty();}

    public boolean haveArticles(){
        return product!= null && !product.isEmpty() &&
                valuePropertyByProduct != null && !valuePropertyByProduct.isEmpty();
    }
    public void chargeMessages( Lang lang ){

        messages = new HashMap<>();
        lang.getTranslatePages().forEach( translatePage -> {
            String key = translatePage.getKeyText().getText();
            key = Arrays.stream(key.split("\\.")).reduce((total,text)-> total +text ).get();
            messages.put( key, translatePage.getTranslate());
        }
        );
    }

    public void chargeArticles( Lang lang ){

        Map<String,String> product = new HashMap<>();
        lang.getTranslateProducts().forEach( translate ->
                product.put(translate.getProduct().getName(),translate.getTranslation())
        );

        valuePropertyByProduct = new HashMap<>();
        lang.getTranslateValueProperties().forEach( translate -> {
            String productName =  translate.getArticle().getProduct().getName();
            if( !valuePropertyByProduct.containsKey( productName ) )
                valuePropertyByProduct.put( productName ,new HashMap<>());

        });
        lang.getTranslateValueProperties().forEach( translate ->
                valuePropertyByProduct.get(translate.getArticle().getProduct().getName()).put(
                        translate.getArticle().getProperty().getName(), translate.getTranslation()
                )
        );
    }



    // return english key
    public String productEnglish( String productName ){
        AtomicReference<String> englishName = new AtomicReference<>(productName);
        product.forEach((english,translate) ->{
            if( translate.equals(productName) ) englishName.set(english);
        });
       return englishName.get();
    }


    public static TranslateDTO createSimpleTranslateDTO(Lang lang) {
        return new TranslateDTO(
                lang.getCode(),
                lang.getName()
        );
    }
}
