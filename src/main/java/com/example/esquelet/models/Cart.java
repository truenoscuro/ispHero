package com.example.esquelet.models;

import com.example.esquelet.dtos.ArticleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private List<ArticleDTO> articles ;

    private Long idCart; ;
    public void add( ArticleDTO article ){
        if(idCart == null) idCart = 0L;
        article.setIdCart(idCart++);
        if( articles == null ) articles = new ArrayList<>();
        articles.add( article );
    }
    public void remove( String product){
        articles = articles.stream().filter(article -> !article.getProduct().equals(product)).toList();
        if(articles.isEmpty()) articles = new ArrayList<>();
    }
    public void removeAll( ){
        idCart = null;
        articles = null;
    }

}
