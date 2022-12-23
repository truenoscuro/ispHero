package com.example.esquelet.services;

import com.example.esquelet.repositories.ArticleRepository;
import com.example.esquelet.repositories.ProductRepository;
import com.example.esquelet.repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    // use for article we sell

    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PropertyRepository propertyRepository;







}
