package com.example.esquelet.repositories;

import com.example.esquelet.entities.Lang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageControler extends JpaRepository<Lang,String> {

}
