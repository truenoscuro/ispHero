package com.example.esquelet.repositories;

import com.example.esquelet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    /*
    Hi ha parameters especial que et deixa cercar només posant les anotacions
    L'altre n'hi ha emprat @Query
     */
    Optional<User> findByUsername(String username);
    //boolean searchUserByUsernameExists(String username);


}
