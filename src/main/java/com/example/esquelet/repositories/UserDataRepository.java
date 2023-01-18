package com.example.esquelet.repositories;

import com.example.esquelet.entities.User;
import com.example.esquelet.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDataRepository extends JpaRepository<UserData,Long> {

    boolean existsByUser( User user );

    UserData findByUser( User user );
}
