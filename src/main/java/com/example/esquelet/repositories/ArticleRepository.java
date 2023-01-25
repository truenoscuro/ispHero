package com.example.esquelet.repositories;

import com.example.esquelet.entities.Article;
import com.example.esquelet.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ArticleRepository extends JpaRepository< Article , Long > {



}

