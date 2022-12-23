package com.example.esquelet.repositories;

import com.example.esquelet.models.User;
import com.example.esquelet.models.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDataRepository extends JpaRepository<UserData,Long> {

    boolean existsByUser( User user );
}
