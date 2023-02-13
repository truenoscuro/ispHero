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



    public User(@NotNull String username, @NotNull String password, @NotNull String email, @NotNull Role role, boolean verified) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.verified = verified;
    }


    @OneToMany(mappedBy = "user")
    private List<Service> services;

    private String username;
    private String password;
    @NotNull
    private String email;
    @NotNull
    @Enumerated( EnumType.STRING )
    private Role role;

    private boolean verified;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<WaitingDomain> waitingDomains;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Invoice> invoices;

}
