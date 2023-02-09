package com.example.esquelet.dtos;

import com.example.esquelet.entities.Role;
import com.example.esquelet.entities.User;
import com.example.esquelet.entities.UserData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    //User
    private Long id;
    private String username;

    private String password;
    private String password2;

    private String email;

    private String role;
    private boolean verified;

    //Personal Data
    private Long idData;
    private String firstName;
    private String lastName1;
    private String lastName2;
    private String address;
    private String city;

    private String postalCode;
    private String country;

    private List<ServiceDTO> services = new ArrayList<>();
    private List<InvoiceDTO> invoices = new ArrayList<>();

    private List<WaitingDomainDTO> waitingDomains = new ArrayList<>();
    private void setUser( User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.verified = user.isVerified();
    }

    public static UserDTO createUserDTO( User user ){
        UserDTO userDTO = new UserDTO();
        userDTO.setUser(user);
        return userDTO;
    }

    public void setUserData( UserData userData ){
        this.idData = userData.getId();
        this.firstName = userData.getFirstName();
        this.lastName1 = userData.getLastName1();
        this.lastName2 = userData.getLastName2();
        this.address = userData.getAddress();
        this.city = userData.getCity();
        this.postalCode = userData.getPostalCode();
        this.country = userData.getCountry();
    }
    public void setUserData( UserDTO userData ){
        this.idData = userData.getIdData();
        this.firstName = userData.getFirstName();
        this.lastName1 = userData.getLastName1();
        this.lastName2 = userData.getLastName2();
        this.address = userData.getAddress();
        this.city = userData.getCity();
        this.postalCode = userData.getPostalCode();
        this.country = userData.getCountry();
    }


    public User getUserEntity(){
        User user =  new User(
                username,
                password,
                email,
                Role.valueOf(role),
                verified
        );
        if(id !=null) user.setId(id);
        return user;
    }
    public UserData getUserDataEntity(){
        UserData userData = new UserData(
                firstName,
                lastName1,
                lastName2,
                address,
                city,
                postalCode,
                getUserEntity()
        );
        if( idData != null ) userData.setId(idData);
        return userData;

    }

    public void addService( ServiceDTO service ){
        if(services == null) services = new ArrayList<>();
        services.add( service );
    }
    public void addInvoice( InvoiceDTO invoice ){
        if(invoices == null) invoices = new ArrayList<>();
        invoices.add( invoice );
    }
    public void addWaitingDomain(WaitingDomainDTO waitingDomain){
        if(waitingDomains == null) waitingDomains = new ArrayList<>();
        waitingDomains.add( waitingDomain );
    }


    public boolean hasData(){
        return idData != null &&
                firstName != null &&
                lastName1 != null &&
                address != null &&
                city != null &&
                postalCode != null;
    }

    // clean User
    public void clean(){
        this.username = null;
        this.password =  null;
        this.email =  null;
        this.role =  null;
        this.verified = false;

        this.firstName = null;
        this.lastName1 = null;
        this.lastName2 = null;
        this.address = null;
        this.city = null;
        this.postalCode = null;
        this.services = null;
        this.country = null;
    }

    public boolean isValid(){
        return username!=null && password!=null;
    }


}
