package com.example.esquelet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = {"tlds"})
public class DomainRegistered {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @NotNull
    private String name;


    @ToString.Exclude
    @ManyToMany(mappedBy = "domains")
    private List<Article> tlds;


    public DomainRegistered(@NotNull String name) {
        this.name = name;
    }
}
