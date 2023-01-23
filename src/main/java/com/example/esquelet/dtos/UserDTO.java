package com.example.esquelet.dtos;

import com.example.esquelet.entities.Role;
import com.example.esquelet.entities.User;
import com.example.esquelet.entities.UserData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    //User
    private String username;

    private String password;
    private String password2;

    private String email;

    private String role;

    //Personal Data
    private String firstName;
    private String lastName1;
    private String lastName2;
    private String address;
    private String city;

    private List<ServiceDTO> services;

    public UserDTO( User user ){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.role = user.getRole().name();
    }
    public UserDTO( User user, UserData userData ){
        this(user);
        setUserData(userData);
    }
    public void setUserData(UserData userData){
        this.firstName = userData.getFirstName();
        this.lastName1 = userData.getLastName1();
        this.lastName2 = userData.getLastName2();
        this.address = userData.getAddress();
        this.city = userData.getCity();
    }


    public User getUserEntity(){
        return new User(
                username,
                password,
                email,
                Role.valueOf(role)
        );
    }

    public void addService( ServiceDTO service ){ services.add( service ); }




    // clean User
    public void clean(){
        this.username = null;
        this.password =  null;
        this.email =  null;
        this.role =  null;

        this.firstName = null;
        this.lastName1 = null;
        this.lastName2 = null;
        this.address = null;
        this.city = null;
        this.services = null;
    }


}
