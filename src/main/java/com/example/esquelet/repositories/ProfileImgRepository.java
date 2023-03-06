package com.example.esquelet.repositories;

import com.example.esquelet.entities.ProfileImg;
import com.example.esquelet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileImgRepository extends JpaRepository<ProfileImg,Long> {

    Optional<ProfileImg> searchByUser(User user);



}
