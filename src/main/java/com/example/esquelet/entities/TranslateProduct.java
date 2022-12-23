package com.example.esquelet.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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

    @NotNull
    @ManyToOne
    private Lang lang;
    @NotNull
    @ManyToOne
    private Product product;

    @NotNull
    private String translation;
}
