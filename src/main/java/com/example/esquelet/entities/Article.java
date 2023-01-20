package com.example.esquelet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@EqualsAndHashCode(exclude = {"product","property","articlesChildren","articleFather"})
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ToString.Exclude
    @NotNull
    @ManyToOne
    private Product product;
    @NotNull
    @ManyToOne
    private Property property;
    @ToString.Exclude
    @ManyToOne
    private Article articleFather;
    @OneToMany(mappedBy = "articleFather")
    private List<Article> articlesChildren;

    private String valueProperty;


}
