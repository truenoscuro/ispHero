package com.example.esquelet.dtos;

import com.example.esquelet.entities.Article;
import com.example.esquelet.entities.DomainRegistered;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainRegisteredDTO {

    private String name;
    private List<String> domainsRegistered;



/*
    public static DomainRegisteredDTO createDomainRegisteredDTO(DomainRegistered domainRegistered){
        return new DomainRegisteredDTO(
                domainRegistered.getName(),
                domainRegistered.getTlds()
                        .stream().map(Article::getValueProperty)
                        .toList()
        );
    }

 */

}
