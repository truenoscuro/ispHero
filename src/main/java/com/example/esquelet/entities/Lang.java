package com.example.esquelet.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = {"properties","translateProducts","translateProperties","translateCategories","translateValueProperties"})
public class Lang {
    @Id
    private String code;
    @NotNull
    private String name;


    @OneToMany(mappedBy = "lang",fetch = FetchType.EAGER)
    private List<TranslateProduct> translateProducts;
    @OneToMany(mappedBy = "lang",fetch = FetchType.EAGER)
    private List<TranslateProperty> translateProperties;

    @OneToMany(mappedBy = "lang",fetch = FetchType.EAGER)
    private List<TranslateCategory> translateCategories;


    @OneToMany(mappedBy = "lang",fetch = FetchType.EAGER)
    private List<TranslateValueProperty> translateValueProperties;





}
