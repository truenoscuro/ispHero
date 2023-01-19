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

    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @NotNull
    @Enumerated( EnumType.STRING )
    private Role role;
}
