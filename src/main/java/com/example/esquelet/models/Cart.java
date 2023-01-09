package com.example.esquelet.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    List<ArticleSell> cart;

    public void add( ArticleSell articleSell ){ cart.add( articleSell ); }
    public void remove( ArticleSell articleSell ){ cart.remove( articleSell ); }
    public void removeAll( ){ cart = new ArrayList<>( ); }

}
