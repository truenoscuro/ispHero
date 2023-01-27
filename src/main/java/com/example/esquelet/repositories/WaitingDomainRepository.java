package com.example.esquelet.repositories;

import com.example.esquelet.entities.Article;
import com.example.esquelet.entities.User;
import com.example.esquelet.entities.WaitingDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitingDomainRepository extends JpaRepository<WaitingDomain,Long> {

    List<WaitingDomain> findAllByNameDomain(String nameDomain);

    Optional<WaitingDomain> findWaitingDomainByUserAndTld(User user , Article article);

    List<WaitingDomain> findAllByUser(User user);
}
