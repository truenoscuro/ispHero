package com.example.esquelet.repositories;

import com.example.esquelet.entities.User;
import com.example.esquelet.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserDataRepository extends JpaRepository<UserData,Long> {

    Optional<UserData> searchByUser(User user);
    boolean existsByUser( User user );
}
