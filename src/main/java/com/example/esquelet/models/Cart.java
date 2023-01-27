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
    private List<ArticleDTO> articles = new ArrayList<>();
    public void add( ArticleDTO article ){ articles.add( article ); }
    public void remove( ArticleDTO article ){ articles.remove( article ); }
    public void removeAll( ){ articles = new ArrayList<>( ); }

}
