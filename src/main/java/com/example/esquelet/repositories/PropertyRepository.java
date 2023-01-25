package com.example.esquelet.repositories;

import com.example.esquelet.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository< Property , Long > {



}
