package com.example.esquelet.repositories;

import com.example.esquelet.entities.Category;
import com.example.esquelet.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> getAllByCategory(Category category);
}
