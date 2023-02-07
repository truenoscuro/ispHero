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

    private String domainName = "";

    private String name = "";
    private Map<String,String> property;

    private List<String> years;
    private Map<String,String> priceYear;
    private List<ArticleDTO> bundle;

    // dates cartBuy
    private String priceBuy = null;
    private String quantity = null;
    private String vat = null;

    public ArticleDTO(String product,
                      String category,
                      Map<String, String> property,
                      List<String> years,
                      Map<String, String> priceYear,
                      List<ArticleDTO> bundle) {
        this.product = product;
        this.category = category;
        this.property = property;
        this.years = years;
        this.priceYear = priceYear;
        this.bundle = bundle;
    }

    public  static ArticleDTO  createArticleDTO(List<Article> articles ){
        //price
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
        return  new ArticleDTO(
                articles.get(0).getProduct().getCategory().getName(),
                articles.get(0).getProduct().getName(),
                generateProperty( articles ),
                years,
                priceYear,
                generateBundle(articles)
                );
    }
    private static Map<String,String> generateProperty(List<Article> articles ){
        Map<String,String> property  = new HashMap<>();
        articles.stream()
                .filter( article -> !article.getProperty().getName().contains("price"))
                .forEach( article ->
                    property.put( article.getProperty( ).getName(),article.getValueProperty() )
                );
        return property;
    }
    private static List<ArticleDTO> generateBundle(List<Article> articles){
        List<ArticleDTO> bundle = new ArrayList<>();
        articles.stream()
                .filter( article ->  article.getProperty().getName().equals( "isBundle" ) )
                .findAny()
                .ifPresent( article -> article.getArticlesChildren()
                        .stream().collect(Collectors.groupingBy( Article::getProduct ) )
                        .values()
                        .stream().map( ArticleDTO::createArticleDTO )
                        .forEach( bundle::add ) );
        return bundle;
    }


    public void addProperty(String key,String value){
        property.put(key,value);
    }

}
