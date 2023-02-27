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
@EqualsAndHashCode(exclude = {
        "properties",
        "translateProducts",
        "translateProperties",
        "translateCategories",
        "translateValueProperties",
        "translatePage"
})
public class Lang {
    @Id
    private String code;
    @NotNull
    private String name;


    @OneToMany(mappedBy = "lang")
    private List<TranslateProduct> translateProducts;
    @OneToMany(mappedBy = "lang")
    private List<TranslateProperty> translateProperties;

    @OneToMany(mappedBy = "lang")
    private List<TranslateCategory> translateCategories;

    @OneToMany(mappedBy = "lang")
    private List<TranslateValueProperty> translateValueProperties;

    @OneToMany(mappedBy = "lang",fetch = FetchType.EAGER)
    private  List<TranslatePage> translatePages;





}
