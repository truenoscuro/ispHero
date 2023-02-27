package com.example.esquelet.entities;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = {"lines","user"})
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToMany(mappedBy = "invoice")
    private List<InvoiceLine> lines;


    private float total;
    @ToString.Exclude
    @NotNull
    @ManyToOne
    private User user;


    @NotNull
    private String fullName;

    @NotNull
    private LocalDateTime dateBuy;





}
