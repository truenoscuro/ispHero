package com.example.esquelet.repositories;

import com.example.esquelet.entities.User;
import com.example.esquelet.entities.WaitingDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingDomainRepository extends JpaRepository<WaitingDomain,Long> {

    List<WaitingDomain> findAllByNameDomain(String nameDomain);

    List<WaitingDomain> findAllByUser(User user);
}
