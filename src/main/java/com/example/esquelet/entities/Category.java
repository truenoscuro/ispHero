package com.example.esquelet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = {"categoriesChildren,categoryFather,products"})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @NotNull
    private String name;

    @OneToMany
    private List<Category> categoriesChildren;
    @ToString.Exclude
    @ManyToOne
    private Category categoryFather;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
