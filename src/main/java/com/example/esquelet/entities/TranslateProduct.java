package com.example.esquelet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
@EqualsAndHashCode(exclude = {"lang","product"})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class TranslateProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ToString.Exclude
    @NotNull
    @ManyToOne
    private Lang lang;

    @NotNull
    @ManyToOne
    private Product product;

    @NotNull
    private String translation;
}
