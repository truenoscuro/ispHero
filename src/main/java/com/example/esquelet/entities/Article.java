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
@EqualsAndHashCode(exclude = {"product","property","articleChildren"})
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Product product;
    @NotNull
    @ManyToOne
    private Property property;
    @ManyToOne
    private Article articleChildren;

    private String valueProperty;


    public int compareTo(Article article){
        return (int) (article.getProduct().getId() - product.getId());
    }


}
