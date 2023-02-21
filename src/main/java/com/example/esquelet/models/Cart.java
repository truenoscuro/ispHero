package com.example.esquelet.models;

import com.example.esquelet.dtos.ArticleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Cart {
    private List<ArticleDTO> articles ;

    private float total;

    private Long idCart; ;

    public Cart (){
        removeAll();
    }
    public void add( ArticleDTO article ){
        article.setIdCart(idCart++);
        articles.add( article );
        //update total cart
        total += Float.parseFloat( article.getPriceBuy() );
    }
    public void remove( Long  id ){
        ArticleDTO article = getArticle(id);
        articles.remove(article);
        total -= Float.parseFloat(article.getPriceBuy());
    }

    public ArticleDTO getArticle( Long  id ){
        //System.out.println(articles.stream().filter(article -> Objects.equals(article.getIdCart(), id)).findFirst().get());
        return articles.stream().filter(article -> Objects.equals(article.getIdCart(), id)).findFirst().get();
    }
    public void removeAll( ){
        idCart = 0L;
        total = 0;
        articles = new ArrayList<>();
    }

}
