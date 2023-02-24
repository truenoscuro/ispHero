package com.example.esquelet.services;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.InvoiceDTO;
import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.entities.Invoice;
import com.example.esquelet.entities.InvoiceLine;
import com.example.esquelet.entities.User;
import com.example.esquelet.models.Cart;
import com.example.esquelet.repositories.InvoiceLineRepository;
import com.example.esquelet.repositories.InvoiceRepository;
import com.example.esquelet.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceLineRepository invoiceLineRepository;

    public void getInvoices( UserDTO user ){
        userRepository.findByUsername( user.getUsername() )
                .ifPresent( u -> u.getInvoices().stream()
                        .map( InvoiceDTO::createInvoiceDTO )
                        .forEach( user::addInvoice )
                );
    }

    @Transactional
    public void addInvoiceByUser(UserDTO userDTO, Cart cart){
        User user = userRepository.findByUsername(userDTO.getUsername()).get();
        List<ArticleDTO> articles = cart.getArticles();
        // Invoice save
        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setDateBuy(LocalDateTime.now());
        invoice.setFullName("Factura-" + DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDateTime.now()));
        invoice.setTotal( cart.getTotal());
        invoiceRepository.save(invoice);
        invoiceRepository.flush();
        //InvoiceLine Save
        articles.stream().map( article -> {
            InvoiceLine invoiceLine = new InvoiceLine();
            invoiceLine.setInvoice( invoice );
            setInvoiceLine( invoiceLine , article );
            return invoiceLine;
        }).forEach( invoiceLine -> invoiceLineRepository.save(invoiceLine));
    }

    private  void setInvoiceLine(InvoiceLine invoiceLine, ArticleDTO articleDTO){
        Map<String,String> properties = articleDTO.getProperty();
        invoiceLine.setNameArticle( articleDTO.getCategory() );
        invoiceLine.setPrice( articleDTO.getPriceBuy() );
        invoiceLine.setQuantity(articleDTO.getQuantity());
        invoiceLine.setVat(articleDTO.getVat());
    }


}
