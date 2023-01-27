package com.example.esquelet.entities;


import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@EqualsAndHashCode(exclude = {"user"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    public User(@NotNull String username, @NotNull String password, @NotNull String email, @NotNull Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }


    @OneToMany(mappedBy = "user")
    private List<Service> services;

    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @NotNull
    @Enumerated( EnumType.STRING )
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<WaitingDomain> waitingDomains;
}
