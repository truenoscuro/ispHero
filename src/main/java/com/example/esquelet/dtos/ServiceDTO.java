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

    private String product;

    private String nameDomain;

    private LocalDateTime dateExpired;

    private boolean isCancelled;





    public static ServiceDTO createServiceDTO( Service service ){
        String product = service.getArticle().getProduct().getName();
        String nameDomain = service.getNameDomain();
        LocalDateTime dateExpired = service.getDateExpired();
        boolean isCancelled = service.isCancelled();
        return new ServiceDTO(product,nameDomain,dateExpired,isCancelled);
    }


}
