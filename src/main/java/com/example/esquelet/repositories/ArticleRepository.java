package com.example.esquelet.repositories;

import com.example.esquelet.entities.Article;
import com.example.esquelet.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository< Article , Long > {

    List< Article > getAllByProduct(Product product);
    List< Article > getAllByArticleChildren(Article article);

}

