package com.example.esquelet.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName1;
    private String lastName2;
    @NotNull
    private String address;
    @NotNull
    private String city;

    private String postalCode;
    private String country;

    @NotNull
    @OneToOne
    private User user;


    public UserData(@NotNull String firstName, @NotNull String lastName1, String lastName2, @NotNull String address, @NotNull String city, @NotNull User user) {
        this.firstName = firstName;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.user = user;
    }
}
