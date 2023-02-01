package com.example.esquelet.services;

import com.example.esquelet.dtos.DomainRegisteredDTO;
import com.example.esquelet.entities.DomainRegistered;
import com.example.esquelet.repositories.DomainRegisteredRepository;
import com.example.esquelet.repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DomainRegisteredService {

    @Autowired
    private DomainRegisteredRepository domainRegisteredRepository;

    public DomainRegisteredDTO getDomainRegisteredDTO(String nameDomain) {
        return domainRegisteredRepository.searchByName(nameDomain)
                .map(DomainRegisteredDTO::createDomainRegisteredDTO)
                .orElseGet(DomainRegisteredDTO::new);
    }
}
