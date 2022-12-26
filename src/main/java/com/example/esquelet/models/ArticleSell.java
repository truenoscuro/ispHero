package com.example.esquelet.models;

import com.example.esquelet.entities.Article;
import com.example.esquelet.entities.Product;
import com.example.esquelet.entities.Property;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSell {

    private String product; // name product Product Domini is named for IspName
    //1- NameProperty  2-{ typeProperty , value }
    Map< String , String [] > properties;

    List< ArticleSell > childrenList;

    public ArticleSell(List< Article > articleList ){
        this.product = articleList.get( 0 ).getProduct().getName();
        constructMap( articleList );
    }
    public ArticleSell( List< Article > articleList , List <Article> childrenList) {
        this( articleList );
        constructChildrenList( childrenList );
    }


    private void constructMap( List< Article > articleList ){
        properties = new HashMap<>();
        articleList.forEach( article -> {
            Property property = article.getProperty();
            properties.put( property.getName() , new String[]{
                    property.getType(),
                    article.getValueProperty()});
        });
    }

    private void constructChildrenList ( List <Article> childrenList){
        this.childrenList = new ArrayList<>();
        childrenList.stream()
                .collect( Collectors.groupingBy( Article::getProduct ) ) // grouping by same Product
                .forEach( ( key ,articleList ) -> this.childrenList.add(
                        new ArticleSell( articleList )
                ));
    }

}
