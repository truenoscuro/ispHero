package com.example.esquelet.services;

import com.example.esquelet.dtos.*;
import com.example.esquelet.entities.*;
import com.example.esquelet.models.Cart;
import com.example.esquelet.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
        String mail = user.getEmail();
        String subject = "Register";
        String text = "Welcome to ISP Hero";

    }


    public void addUserData(UserDTO user){
        userDataRepository.save(user.getUserDataEntity());
    }



    @Transactional
    public void buy(UserDTO userDTO, Cart cart){
        /*
        1- Agaf el articleDTO --> transform a article
        1,1 --> Miro si te nameDomain i el guard
        2- article --> guard a Service  amb nameDomain si en te
        3- article --> guard la factura

        4- article --> MIro si està en wite list?
         */
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
                    addService( articles.get(i).getDomainName(),
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
