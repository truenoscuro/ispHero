package com.example.esquelet.entities;


import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = {"category","articles"})
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @ToString.Exclude
    @NotNull
    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<Article> articles ;

}
