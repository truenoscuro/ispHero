package com.example.esquelet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = {"user","article"})
public class WaitingDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private Article tld;

    @NotNull
    private String nameDomain;


    public WaitingDomain(@NotNull String nameDomain,@NotNull User user, @NotNull Article tld) {
        this.user = user;
        this.tld = tld;
        this.nameDomain = nameDomain;
    }
}
