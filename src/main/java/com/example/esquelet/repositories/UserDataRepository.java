package com.example.esquelet.repositories;

import com.example.esquelet.entities.User;
import com.example.esquelet.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserData,Long> {

    boolean existsByUser( User user );
}
