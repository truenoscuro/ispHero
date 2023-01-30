package com.example.esquelet.dtos;

import com.example.esquelet.entities.InvoiceLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceLineDTO {

    private String nameArticle;

    private String price;

    private String quantity;

    private  String vat;


    public static InvoiceLineDTO createInvoiceLineDTO(InvoiceLine invoiceLine){
        return new InvoiceLineDTO(
                invoiceLine.getNameArticle(),
                invoiceLine.getPrice(),
                invoiceLine.getQuantity(),
                invoiceLine.getVat()
        );
    }

}
