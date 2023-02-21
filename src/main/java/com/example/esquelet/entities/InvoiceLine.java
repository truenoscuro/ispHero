package com.example.esquelet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = {"invoice"})
public class InvoiceLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nameArticle;

    @NotNull
    private String price;
    @NotNull
    private String quantity;
    @NotNull
    private String vat;


    @ToString.Exclude
    @NotNull
    @ManyToOne
    private Invoice invoice;





}
