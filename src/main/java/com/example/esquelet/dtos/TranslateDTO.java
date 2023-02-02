package com.example.esquelet.dtos;

import com.example.esquelet.entities.Article;
import com.example.esquelet.entities.Lang;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    private Map<String,Map<String,String>> valueCategory;

    public TranslateDTO(String code, String name){
        this.code = code;
        this.name = name;
    }


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
        Map<String,Map<String,String>> valueCategory = new HashMap<>();
        lang.getTranslateValueProperties().forEach( translate -> {
            if( !valueCategory.containsKey( translate.getArticle().getProduct().getName() ) ){
                valueCategory.put(translate.getArticle().getProduct().getName(),new HashMap<>());
            }
        });
        lang.getTranslateValueProperties().forEach( translate ->{
            valueCategory.get(translate.getArticle().getProduct().getName()).put(
                    translate.getArticle().getProperty().getName(), translate.getTranslation()
            );
        });
        return  new TranslateDTO(code,name,product,property,category,valueCategory);



    }




}
