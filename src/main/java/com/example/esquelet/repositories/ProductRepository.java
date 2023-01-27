package com.example.esquelet.repositories;

import com.example.esquelet.entities.Category;
import com.example.esquelet.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> getAllByCategory(Category category);

    Optional<Product> getProductsByName(String name);
}
