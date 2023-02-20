package com.example.esquelet.repositories;

import com.example.esquelet.entities.Lang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Lang,String> {

    Optional< Lang > findByCode(String code);
}
