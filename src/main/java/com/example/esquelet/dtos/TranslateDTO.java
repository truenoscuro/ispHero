package com.example.esquelet.dtos;

import com.example.esquelet.entities.Lang;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

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
        //------------
        return  new TranslateDTO(code,name,product,property,category);


    }




}
