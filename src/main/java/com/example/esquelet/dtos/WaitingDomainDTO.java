package com.example.esquelet.dtos;

import com.example.esquelet.entities.Article;
import com.example.esquelet.entities.DomainRegistered;
import com.example.esquelet.entities.WaitingDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WaitingDomainDTO {

    private String nameDomain;

    private List<String> tlds;

    public static WaitingDomainDTO createWaitingDomainDTO(List<WaitingDomain> waitingDomains){
        return new WaitingDomainDTO(
                waitingDomains.get(0).getNameDomain(),
                waitingDomains.stream()
                        .map(WaitingDomain::getTld)
                        .map(Article::getValueProperty)
                        .toList()
        );
    }

}
