package com.example.esquelet.repositories;

import com.example.esquelet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    /*
    Hi ha parametres especial que te deixa cercar nomes posant les anotacions
    L'altre en hi ha empleant @Query
     */
    Optional<User> findByUsername(String username);
    //boolean searchUserByUsernameExists(String username);


}
