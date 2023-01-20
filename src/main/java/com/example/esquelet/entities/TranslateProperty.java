package com.example.esquelet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = {"lang","property"})
public class TranslateProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @NotNull
    @ManyToOne
    private Lang lang;
    @NotNull
    @ManyToOne
    private Property property;

    @NotNull
    private String translation;
}
