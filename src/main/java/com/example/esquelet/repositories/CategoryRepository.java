package com.example.esquelet.repositories;

import com.example.esquelet.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository< Category , Long > {
    Optional< Category > searchById( String id );
    Optional< Category > searchByName( String name );


}
