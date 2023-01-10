package com.example.esquelet.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = {"article","user"})
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Article article;

    @NotNull
    @ManyToOne
    private User user;

    /*
    @NotNull
    @UpdateTimestamp
    private Timestamp dateBuy;
     */
    private LocalDateTime dateExpired;

    private boolean isCancelled;
    private String nameDomain;





}
