package com.example.esquelet.dtos;

import com.example.esquelet.entities.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {
    private Long id;

    private String product;

    private String nameDomain;

    private LocalDateTime dateExpired;

    private boolean isCancelled;


    public static ServiceDTO createServiceDTO( Service service ){
        return new ServiceDTO(
                service.getId(),
                service.getArticle().getProduct().getName(),
                service.getNameDomain(),
                service.getDateExpired(),
                service.isCancelled()
        );
    }
}
