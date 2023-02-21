package com.example.esquelet.dtos;


import com.example.esquelet.entities.Invoice;
import com.example.esquelet.entities.InvoiceLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {

    private String fullName;

    private LocalDateTime dateBuy;

    private List<InvoiceLineDTO> lines;

    public static InvoiceDTO createInvoiceDTO(Invoice invoice){
        return new InvoiceDTO(
                invoice.getFullName(),
                invoice.getDateBuy(),
                invoice.getLines().stream()
                        .map(InvoiceLineDTO::createInvoiceLineDTO)
                        .toList()
        );
    }


}
