package com.example.esquelet.repositories;

import com.example.esquelet.entities.User;
import com.example.esquelet.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
@Repository
public interface UserDataRepository extends JpaRepository<UserData,Long> {

    Optional<UserData> searchByUser(User user);
    boolean existsByUser( User user );

    UserData findByUser( User user );
}
