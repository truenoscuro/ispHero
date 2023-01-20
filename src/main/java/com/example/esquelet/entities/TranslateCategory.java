package com.example.esquelet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = {"lang","category"})
public class TranslateCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ToString.Exclude
    @NotNull
    @ManyToOne
    private Lang lang;

    @NotNull
    @ManyToOne
    private Category category;

    @NotNull
    private String translation;
}
