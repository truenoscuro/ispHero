package com.example.esquelet.services;

import com.example.esquelet.repositories.DomainRegisteredRepository;
import com.example.esquelet.repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainRegisteredService {


    @Autowired
    private DomainRegisteredRepository domainRegisteredRepository;
    @Autowired
    private PropertyRepository propertyRepository;


}
