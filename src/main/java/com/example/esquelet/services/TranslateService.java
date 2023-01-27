package com.example.esquelet.services;

import com.example.esquelet.dtos.TranslateDTO;
import com.example.esquelet.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranslateService {

    @Autowired
    private LanguageRepository languageRepository;

    public List<TranslateDTO> getAll(){
        return languageRepository.findAll().stream().map(TranslateDTO::createTranslateDTO).toList();
    }

}
