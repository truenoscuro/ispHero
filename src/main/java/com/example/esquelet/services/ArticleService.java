package com.example.esquelet.services;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.entities.Product;
import com.example.esquelet.repositories.CategoryRepository;
import com.example.esquelet.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    // use for article we sell
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    public List<ArticleDTO> getArticleDTOList(String categoryName) {
        return productRepository
                .getAllByCategory( categoryRepository.searchByName(categoryName).get() )
                .stream( ).map( this::getArticleDTO )
                .toList( );
    }
    private ArticleDTO getArticleDTO(Product product){
        return ArticleDTO.createArticleDTO(product.getArticles());
    }

    public ArticleDTO getArticleDTO(String productName) {
        return getArticleDTO(productRepository.getProductsByName(productName).get());
    }

}










