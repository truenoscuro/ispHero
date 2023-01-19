package com.example.esquelet.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Cart {
    List<ArticleSell> articles;

    public Cart(){ articles = new ArrayList<>(); }

    public void add( ArticleSell articleSell ){ articles.add( articleSell ); }
    public void remove( ArticleSell articleSell ){ articles.remove( articleSell ); }
    public void removeAll( ){ articles = new ArrayList<>( ); }

}
