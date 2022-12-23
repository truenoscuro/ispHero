package com.example.esquelet.repositories;

import com.example.esquelet.models.User;
import org.aspectj.apache.bcel.util.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    /*
    Hi ha parametres especial que te deixa cercar nomes posant les anotacions
    L'altre en hi ha empleant @Query
     */
    Optional<User> searchUserByUsernameEquals(String username);
    boolean searchUserByUsernameExists(String username);


}
