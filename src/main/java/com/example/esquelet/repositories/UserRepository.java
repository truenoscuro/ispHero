package com.example.esquelet.repositories;

import com.example.esquelet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    /*
    Hi ha parameters especial que et deixa cercar només posant les anotacions
    L'altre n'hi ha emprat @Query
     */
    Optional<User> searchUserByUsernameEquals(String username);

    @Query("SELECT u FROM User u WHERE u.username = :user")
    Object findByUsername(String user);
    //boolean searchUserByUsernameExists(String username);


}
