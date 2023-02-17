package com.example.esquelet.dtos;

import com.example.esquelet.entities.Lang;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Map<String,String> category;

    private Map<String,Map<String,String>> valuePropertyByProduct;



    public static TranslateDTO createTranslateDTO(Lang lang){
        String code = lang.getCode();
        String name = lang.getName();
        //------------
        Map<String,String> product = new HashMap<>();
        lang.getTranslateProducts().forEach(translate ->
                        product.put(translate.getProduct().getName(),translate.getTranslation()));
        //------------
        Map<String,String> property = new HashMap<>();
        lang.getTranslateProperties().forEach(translate ->
                property.put(translate.getProperty().getName(),translate.getTranslation()));
        //------------
        Map<String,String> category = new HashMap<>();
        lang.getTranslateCategories().forEach(translate ->
                category.put(translate.getCategory().getName(),translate.getTranslation()));
        //------------ product1.tld
        Map<String,Map<String,String>> valuePropertyByProduct = new HashMap<>();
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
        return  new TranslateDTO(code,name,product,property,category,valuePropertyByProduct);



    }



    // return english key

    public String productEnglish( String productName ){
        AtomicReference<String> englishName = new AtomicReference<>("");
        product.forEach((k,v) ->{
            if( v.equals(productName) ) englishName.set(k);
        });
       return englishName.get();
    }
    public String propertyEnglish( String propertyName ){
        AtomicReference<String> englishName = new AtomicReference<>("");
        product.forEach((k,v) ->{
            if( v.equals(propertyName) ) englishName.set(k);
        });
        return englishName.get();
    }

    public String categoryEnglish( String categoryName ){
        AtomicReference<String> englishName = new AtomicReference<>("");
        product.forEach((k,v) ->{
            if( v.equals(categoryName) ) englishName.set(k);
        });
        return englishName.get();
    }









}
