package com.example.esquelet.dtos;

import com.example.esquelet.entities.Article;
import com.example.esquelet.entities.Property;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    private String product;

    private String category;

    private String domainName;

    private String name;
    private Map<String,String> property;

    private List<String> years;
    private Map<String,String> priceYear;
    private Map<String ,String> typeProperty;
    private List<ArticleDTO> bundle;

    public  static ArticleDTO  createArticleDTO(List<Article> articles ){
        //product name
        String category = articles.get(0).getProduct().getCategory().getName();
        String product = articles.get(0).getProduct().getName();
        // propertyes and typePropertyes
        Map<String,String> property  = new HashMap<>();
        Map<String,String> typeProperty  = new HashMap<>();

        articles.stream()
                .filter( article -> !article.getProperty().getName().contains("price"))
                .forEach( article -> {
                    Property propertyArticle = article.getProperty( );
                    property.put( propertyArticle.getName(),article.getValueProperty() );
                    typeProperty.put( propertyArticle.getName(),propertyArticle.getType() );
                });

        Map<String,String> priceYear = new HashMap<>();
        List<String> years = new ArrayList<>();
        articles.stream()
                .filter(article -> article.getProperty().getName().contains("price"))
                .forEach( article ->{
                    String year = article.getProperty().getName().replaceAll("[^0-9]", "");
                    if(year.equals("")) year = "0";
                    years.add( year );
                    priceYear.put( year , article.getValueProperty( ) );
                });
        // DEFAULT PROVES
        property.put("priceBuy","default");
        property.put("quantity","default");
        property.put("vat","default");
        //bundle
        List<ArticleDTO> bundle = new ArrayList<>();
        articles.stream().filter( article ->  article.getProperty().getName().equals( "isBundle" ) )
                .findAny()
                .ifPresent( article -> article.getArticlesChildren()
                        .stream().collect(Collectors.groupingBy( Article::getProduct ) )
                        .values()
                        .stream().map( ArticleDTO::createArticleDTO )
                        .forEach( bundle::add ) );
        return  new ArticleDTO( category,
                product,
                "",
                "",
                property,
                years,
                priceYear,
                typeProperty,
                bundle);
    }


    public void addProperty(String key,String value){
        property.put(key,value);
    }

}
