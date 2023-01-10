package com.example.esquelet.services;

import com.example.esquelet.entities.Article;
import com.example.esquelet.entities.Category;
import com.example.esquelet.entities.Product;
import com.example.esquelet.models.ArticleSell;
import com.example.esquelet.repositories.ArticleRepository;
import com.example.esquelet.repositories.CategoryRepository;
import com.example.esquelet.repositories.ProductRepository;
import com.example.esquelet.repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleSellService {
    // use for article we sell
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PropertyRepository propertyRepository;


    public boolean existCategory( String idCategory ){
        return categoryRepository.searchById(idCategory).isPresent();
    }

    public List< ArticleSell > getListArticleSellList( String idCategory ){
        Optional< Category > categoryOptional = categoryRepository.searchById( idCategory );
        Category category = categoryOptional.get(); // check in Controller
        List< Product > productList = productRepository.getAllByCategory( category );
        return  productList.stream().map( product -> {
            List<Article> articleList = articleRepository.getAllByProduct( product );
            List <Article> childrenList = articleRepository.getAllByArticleChildren(articleList.get(0));
            if( !articleList.isEmpty() ){
                return new ArticleSell( articleList , childrenList ) ;
            }
            return new ArticleSell();
        }).toList();
    }







}
