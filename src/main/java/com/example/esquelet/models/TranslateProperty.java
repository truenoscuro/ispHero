package com.example.esquelet.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class TranslateCategory {

    @Id
    private Long id;

    @NotNull
    @ManyToOne
    private Lang lang;
    @NotNull
    @ManyToOne
    private Category category;

    @NotNull
    private String translate;
}
