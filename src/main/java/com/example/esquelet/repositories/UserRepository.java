package com.example.esquelet.repositories;

import com.example.esquelet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    /*
    Hi ha parametres especial que te deixa cercar nomes posant les anotacions
    L'altre en hi ha empleant @Query
     */
    Optional<User> searchUserByUsernameEquals(String username);

    @Query("SELECT u FROM User u WHERE u.username = :user")
    Object findByUsername(String user);
    //boolean searchUserByUsernameExists(String username);


}
