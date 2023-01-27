package com.example.esquelet.repositories;

import com.example.esquelet.entities.DomainRegistered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DomainRegisteredRepository extends JpaRepository<DomainRegistered, Long > {
    Optional<DomainRegistered> searchByName(String name);
}

