package com.example.esquelet.services;


import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.entities.ProfileImg;
import com.example.esquelet.entities.User;
import com.example.esquelet.repositories.ProfileImgRepository;
import com.example.esquelet.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Base64;

@Service
public class ProfileImgService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileImgRepository profileImgRepository;

    @Transactional
    public void saveProfileImg(UserDTO userDTO , MultipartFile image ) throws IOException {
        //* save encoded img
        User user = userRepository.findById(userDTO.getId()).get();
        ProfileImg img = new ProfileImg(
                user,
                Base64.getEncoder().encodeToString(image.getBytes()),
                image.getContentType()
        );
        profileImgRepository.searchByUser(user).ifPresent(profileImg -> img.setId(profileImg.getId()));
        profileImgRepository.save(img);
    }

    public void getProfileImg( UserDTO userDTO ){
        User user = userRepository.findById(userDTO.getId()).get();
        profileImgRepository.searchByUser( user )
                .ifPresent(profileImg ->
                        userDTO.setProfileImg(
                                "data:"+
                                profileImg.getFormat()+
                                ";charset=utf-8;base64,"
                                +profileImg.getImg())
        );
    }
}

