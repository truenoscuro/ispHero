package com.example.esquelet.services;

import com.example.esquelet.dtos.*;
import com.example.esquelet.entities.*;
import com.example.esquelet.models.Cart;
import com.example.esquelet.repositories.*;
import com.mailersend.sdk.Recipient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.exceptions.MailerSendException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private WaitingDomainRepository waitingDomainRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceLineRepository invoiceLineRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private DomainRegisteredRepository domainRegisteredRepository;
    @Autowired
    private ArticleRepository articleRepository;


    public Optional<User> searchUser( UserDTO user ){
        return userRepository.findByUsername(user.getUsername());
    }

    public UserDTO getUser( UserDTO userDTO ){
        User user = searchUser( userDTO ).get();
        UserDTO userResult = UserDTO.createUserDTO( user );
        userDataRepository.searchByUser( user ).ifPresent(userResult::setUserData);
        return  userResult;
    }


    // boolean if you want newsletter
    @Transactional
    public void addUser(UserDTO user) {
        // if(wantNewsLetter) newsLetterRepository.save(user.email)
        userRepository.save(user.getUserEntity());
    }

    public boolean checkUser( String userName , String password ) {
        Optional<User> user = userRepository.findByUsername(userName);
        return user.map(value -> new BCryptPasswordEncoder().matches(password, value.getPassword())).orElse(false);
    }

    public boolean checkUser( String userName ) {
        Optional<User> user = userRepository.findByUsername(userName);
        return user.isPresent();
    }

    public void getServices( UserDTO user ){
        userRepository.findByUsername( user.getUsername() )
                .ifPresent( userEntity -> userEntity.getServices().stream()
                        .map( ServiceDTO::createServiceDTO )
                        .forEach( user::addService )
                );
    }

    public void getInvoices( UserDTO user ){
        userRepository.findByUsername( user.getUsername() )
                .ifPresent( u -> u.getInvoices().stream()
                        .map( InvoiceDTO::createInvoiceDTO )
                        .forEach( user::addInvoice )
                );
    }
    public void getWaitingDomains(UserDTO user){
        User userEntity = userRepository.findByUsername(user.getUsername()).get();
        waitingDomainRepository.findAllByUser(userEntity)
                .stream().collect(Collectors.groupingBy(WaitingDomain::getNameDomain))
                .values()
                .stream().map(WaitingDomainDTO::createWaitingDomainDTO)
                .forEach(user::addWaitingDomain);
    }

    public void sendRegisterMail( UserDTO user ) {
        Email registerMail = new Email();
        registerMail.setFrom("ISP Hero", "info@isphero.com");
        registerMail.setSubject("Please verify your address");

        Recipient recipient = new Recipient(user.getFirstName(), user.getEmail());

        registerMail.AddRecipient(recipient);

        registerMail.setHtml("<h1>ISP Hero</h1><p>Hi "+user.getUsername()+"</p><p>Thank you for registering with ISP Hero. Please click the link below to verify your email address.</p><p><a href=\"http://localhost:8080/verify/"+user.getUsername()+"\">Verify your email address</a></p><br><hr><br><p>This link will expire in 24 hours.</p><p>If you did not register with ISP Hero, please ignore this email.</p><p>Thanks for your interest in us,</p><p>ISP Hero</p>");

        MailerSend mailerSend = new MailerSend();
        mailerSend.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiOGZhMzlmZTdlMmVkZTM0ODJkZDYyMDRkNzQxMmE1MDJlMmM4YWFlNWJkNmZjNmY0ZWQzY2E4NTY3YTFhYTk2ZmEyOGNjY2FiNTNkYjdjOWUiLCJpYXQiOjE2NzUyNDQ0MTguOTIzMSwibmJmIjoxNjc1MjQ0NDE4LjkyMzEwMywiZXhwIjo0ODMwOTE4MDE4LjkxNjc2Nywic3ViIjoiNTYwOTIiLCJzY29wZXMiOlsiZW1haWxfZnVsbCIsImRvbWFpbnNfZnVsbCIsImFjdGl2aXR5X2Z1bGwiLCJhbmFseXRpY3NfZnVsbCIsInRva2Vuc19mdWxsIiwid2ViaG9va3NfZnVsbCIsInRlbXBsYXRlc19mdWxsIiwic3VwcHJlc3Npb25zX2Z1bGwiLCJzbXNfZnVsbCIsImVtYWlsX3ZlcmlmaWNhdGlvbl9mdWxsIiwiaW5ib3VuZHNfZnVsbCIsInJlY2lwaWVudHNfZnVsbCIsInNlbmRlcl9pZGVudGl0eV9mdWxsIl19.fFXonujENY0bK7HoMPrTHWnH-eWBXwuTePS_DFp75ay010aus_zlony_FXhnB01KGx4XSI-GCEG9iJjuzIaPFivOMv62htYniRsZmKFyDFGLSGJmriC4Fzsl4K9MFO9TF_0m69DocCAGlwkE0SD3NWJS0VyQjqJh1oYZLdif4BzMQQYOy41WFMPSqu665IN120C0ZJU4dZdCW78nhbiM3D-lJl2sdDITflqiSyHcMscmg0R-sr8YD_vZsZ0ju81nH24i7Zx2iT7BtDHkfN9aduB7kO0YlTofT_zFGY6FntHs1Ub1axy6Tlg0Bg_UpoPL3Baf8qIuMlsXnvmPleRP3GKPNUJdRd-SkT4JD8voEtEO6eoNf6Xbz5R6_aSlkIFdNfXYDWfVRR-KzOtuAtQAZZ7JxzC_a1wSAFm9cYndl1C83ZkSrONio8m-uab3cgqmpZ-51ZtxgEfxoJyAMZeoxkNQPjONvGNXgDsmR2ktGeDrTzXgwUpUvEn9tHQkWuwUTzgQRBKGt1rkDiNhzQ9l0D4f0ZVROY6lCEP4yMQq6KWKBf32H3VovE9O3TFBJY9OYx17tnUykWCw81NYx0Z8sx68gZhE_GzBjZKi42aOifcy3k11JhKIF_svcl4dxQDvaUcqUblwlWpcZXSYM-hnyeSlShPqZUQS2W11_zDbw4I");

        try {
            MailerSendResponse response = mailerSend.emails().send(registerMail);
            System.out.println(response.messageId);
        } catch (MailerSendException e) {
            e.printStackTrace();
        }
    }


    public void addUserData(UserDTO user){
        userDataRepository.save(user.getUserDataEntity());
    }



    @Transactional
    public void buy(UserDTO userDTO, Cart cart){

        User user = userRepository.findByUsername(userDTO.getUsername()).get();
        List<ArticleDTO> articles = cart.getArticles();

        // Service Save
        List<Article> articlesEntity = articles.stream()
                .map( a -> productRepository.getProductsByName(a.getProduct())
                        .get().getArticles()
                        .stream().findFirst()
                        .get()
                ).toList();

        for( int i = 0 ; i < articlesEntity.size() ; i++ )
            serviceRepository.save(
                    addService( articles.get(i).getName(),
                            articlesEntity.get(i),
                            user
                    )
            );

        // Invoice save
        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setFullName("factura "+ user.getUsername());
        invoiceRepository.save(invoice);
        invoiceRepository.flush();
        //InvoiceLine Save
        articles.stream().map( article -> {
            InvoiceLine invoiceLine = new InvoiceLine();
            invoiceLine.setInvoice( invoice );
            setInvoiceLine( invoiceLine , article );
            return invoiceLine;
        }).forEach( invoiceLine -> invoiceLineRepository.save(invoiceLine));
        //DomainRegisteredSave
        //<hola , es, com , tv>  <hola>
        articles.stream()
                .filter( article -> !Objects.equals(article.getDomainName(), ""))
                .forEach( article ->{
                    DomainRegistered domain = new DomainRegistered(article.getName());
                    Optional<DomainRegistered> domainOptional = domainRegisteredRepository.searchByName(domain.getName());
                    if(domainOptional.isPresent()){
                        domain =  domainOptional.get();
                    } else {
                        domain = domainRegisteredRepository.save(domain);
                        domainRegisteredRepository.flush();
                    }
                    Article a = articleRepository.searchArticleByValueProperty( article.getProperty().get("tld") ).get();
                    a.getDomains().add(domain);
                    articleRepository.save(a);
                });

    }
    private  void setInvoiceLine(InvoiceLine invoiceLine, ArticleDTO articleDTO){
        Map<String,String> properties = articleDTO.getProperty();
        invoiceLine.setNameArticle( articleDTO.getProduct( ) );
        if( articleDTO.getDomainName() != null ) invoiceLine.setNameArticle(articleDTO.getDomainName() + properties.get("tld"));
        invoiceLine.setPrice( properties.get( "priceBuy" ) );
        if( properties.get("priceBuy") == null ) invoiceLine.setPrice("Free");
        invoiceLine.setQuantity( properties.get( "quantity" ) );
        if( properties.get("quantity") == null) invoiceLine.setQuantity("1");
        invoiceLine.setVat( properties.get("vat") );
    }


    private com.example.esquelet.entities.Service addService(String nameDomain , Article article, User user){
        com.example.esquelet.entities.Service service = new com.example.esquelet.entities.Service();
        service.setArticle(article);
        service.setUser( user );
        service.setDateExpired(LocalDateTime.now().plusYears(1));
        service.setCancelled( false );
        if(nameDomain != null) service.setNameDomain(nameDomain);
        return  service;
    }

}
