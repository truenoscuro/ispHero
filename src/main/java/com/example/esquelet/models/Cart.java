package com.example.esquelet.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Cart {
    List<ArticleSell> articlesBuy;

    public Cart(){ articlesBuy = new ArrayList<>(); }

    public void add( ArticleSell articleSell ){ articlesBuy.add( articleSell ); }
    public void remove( ArticleSell articleSell ){ articlesBuy.remove( articleSell ); }
    public void removeAll( ){ articlesBuy = new ArrayList<>( ); }

}
