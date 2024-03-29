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
    private String category;
    private String product;


    private String domainName = "";

    private String name = "";
    private Map<String,String> property;

    private List<String> years;
    private Map<String,String> priceYear;
    private List<ArticleDTO> bundle;

    // dates cartBuy
    private String year;
    private String priceBuy;
    private String quantity;
    private String vat;

    //Cart id
    private Long idCart;

    // Service user
    private ServiceDTO service ;
    //articleBuy
    private ArticleDTO domainAppend;



    public ArticleDTO(String category,
                      String product,
                      Map<String, String> property,
                      List<String> years,
                      Map<String, String> priceYear,
                      List<ArticleDTO> bundle) {
        this.category = category;
        this.product = product;
        this.property = property;
        this.years = years;
        this.priceYear = priceYear;
        this.bundle = bundle;
    }

    //TODO change vat in database property
    public void  setDatesBuy(ArticleDTO article){
        year = article.getYear( );
        priceBuy = priceYear.get( year );
        quantity = 1 +  " ";
        vat = 21 + " " ;
        // vat = property.get("vat");
    }

    public void setDomainNameAndName( String domainName ){
        this.domainName = domainName;
        this.name = domainName + property.get("tld");
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
        Map<String,String> properties =  generateProperty( articles );
        //-- Aplicate discount;
        if( properties.containsKey("discount") ){
            float discount = (100-Float.parseFloat(properties.get("discount")))/100;
            priceYear.forEach((year,price) -> priceYear.replace(year,(Float.parseFloat(price) * discount)+""));
        }
        return  new ArticleDTO(
                articles.get(0).getProduct().getCategory().getName(),
                articles.get(0).getProduct().getName(),
                properties,
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



        property.put("vat","" + 21); // <-- TODO put in ddbb
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


    public boolean isDomain(){ return category.equals( "domain" ); }



}
