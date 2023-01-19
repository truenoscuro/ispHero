package com.example.esquelet.services;

import com.example.esquelet.dtos.ArticleDTO;
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
public class ArticleService {
    // use for article we sell
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PropertyRepository propertyRepository;


    public boolean existCategory(String idCategory) {
        return categoryRepository.searchById(idCategory).isPresent();
    }

    public List<ArticleDTO> getArticleDTOList(String categoryName) {
        return productRepository
                .getAllByCategory(categoryRepository.searchByName(categoryName).get())
                .stream().map(product -> ArticleDTO.createArticleDTO(product.getArticles()))
                .toList();




    }

    public ArticleDTO getArticleDTO(String productName) {
        return ArticleDTO.createArticleDTO(productRepository.getProductsByName(productName).get().getArticles());
    }

}










