package com.example.esquelet.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ProfileImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @OneToOne
    private User user;


    @NotNull
    private String img;
    @NotNull
    private String format;


    public ProfileImg(@NotNull User user, @NotNull String img, @NotNull String format) {
        this.user = user;
        this.img = img;
        this.format = format;
    }
}
