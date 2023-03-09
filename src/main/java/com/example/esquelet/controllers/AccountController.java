package com.example.esquelet.controllers;

import com.example.esquelet.dtos.ArticleDTO;
import com.example.esquelet.dtos.InvoiceDTO;
import com.example.esquelet.dtos.ServiceDTO;
import com.example.esquelet.dtos.UserDTO;

import com.example.esquelet.entities.Invoice;
import com.example.esquelet.models.Cart;
import com.example.esquelet.models.IdCart;
import com.example.esquelet.repositories.WaitingDomainRepository;
import com.example.esquelet.services.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage","articleComplete","urlCdn"})
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private WaitingDomainService waitingDomainService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ServService servService;

    @Autowired
    private ProfileImgService profileImgService;



    private void chargeUser( Model model ){
        UserDTO userDTO =  userService.getUser((UserDTO) model.getAttribute("user"));
        profileImgService.getProfileImg( userDTO );
        servService.getServices( userDTO );
        invoiceService.getInvoices( userDTO );
        waitingDomainService.getWaitingDomainsByUser( userDTO );

        model.addAttribute("user",userDTO);
    }

    @GetMapping("/account")
    public String account( Model model ) {
        chargeUser( model );
        model.addAttribute("userPassword" , new UserDTO() );
        return "backendUser/account";
    }

    @GetMapping(value = "/account/{page}/{action}")
    public String redirect(@PathVariable String page,
                           @PathVariable(required = false) String action ){
        if(!page.equals("services") || action == null) return "redirect:/account";
        if(action.equals("add")) return "redirect:/account/services";
        if(Integer.parseInt(action) >= 0) return "redirect:/account/service/expire/{idService}";
        return "redirect:/account" ;

    }

    @PostMapping("/account/vincule")
    public String vincule( @RequestParam("idService") Long idService, Model model ){
        UserDTO user = (UserDTO) model.getAttribute("user");

        ServiceDTO service = user.getServices()
                .stream()
                .filter(serviceDTO-> Objects.equals(serviceDTO.getId(), idService) )
                .findFirst()
                .get();

        Long idCart = ((IdCart) model.getAttribute("articleComplete")).getId();

        ArticleDTO article = ((Cart) model.getAttribute("cartUser")).getArticle( idCart );

        article.getProperty().replace("needDomain","false");
        article.setService( service );

        return "redirect:/cart";
    }

    @GetMapping("/account/services")
    public String addService(Model model){
        if(model.containsAttribute("articleComplete")
                && ((IdCart) model.getAttribute("articleComplete")).getId() != null
        ) {
            chargeUser(model);
            return "backendUser/services";
        }
        return "redirect:/account";
    }
    @GetMapping("account/service/expire/{id}")
    public String updateService(@PathVariable("id") Long idService){
        servService.changeExpired( idService );
        return "redirect:/account";
    }




    @PostMapping("/account/waitingdomains")
    public String modifyWaitingDomain( @ModelAttribute ArticleDTO articleWaiting, Model model){
        waitingDomainService.addByUser(
                articleWaiting.getDomainName(),
                ( ( UserDTO ) model.getAttribute("user") ),
                articleWaiting.getProduct()
        );
        return "redirect:/account";
    }

    @PostMapping("/account/delete-waiting")
    public String deleteWaitingDomain( @ModelAttribute ArticleDTO articleWaiting, Model model){
        waitingDomainService.delete(
                articleWaiting.getDomainName(),
                ( ( UserDTO ) model.getAttribute("user") ),
                articleWaiting.getProduct()
        );
        return "redirect:/account";
    }

    @PostMapping("/account/update")
    public String updateUserData(@ModelAttribute UserDTO userData , Model model){
        UserDTO user = (UserDTO) model.getAttribute("user");
        user.setUserData(userData);
        userService.addUserData(user);
        return "redirect:/account";
    }

    @PostMapping("/account/changePassword")
    public String updatePassword(@ModelAttribute UserDTO userPassword, Model model){
        UserDTO user = (UserDTO) model.getAttribute("user");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if( !bCryptPasswordEncoder.matches( userPassword.getPassword() ,  user.getPassword() ) ){
            // TODO need confirm to fail
            // password original diff
            System.out.println("password original diff");
            return "redirect:/account";
        }
        if( !userPassword.getPassword2().equals( userPassword.getPasswordRepeat( ) ) ){
            // password diff
            System.out.println("new passwords diff");
            return "redirect:/account";
        }
        String newPassword = bCryptPasswordEncoder.encode( userPassword.getPassword2( ) );
        userService.updatePassword( user , newPassword );
        return "redirect:/account";
    }

    @PostMapping("/account/updateProfileImg")
    public String updateProfileImg(@RequestParam("image") MultipartFile imageFile, Model model) throws IOException {
        profileImgService.saveProfileImg(
                (UserDTO) Objects.requireNonNull(model.getAttribute("user")),
                imageFile
        );
        return "redirect:/account";
    }

    @GetMapping("/account/invoice/{id}")
    public ResponseEntity<byte[]> getInvoice(@PathVariable("id") Long idInvoice, Model model) throws DocumentException {
        // Get Invoice by id
        InvoiceDTO invoice = invoiceService.getInvoiceDTOByID( idInvoice );
        if (invoice == null) return ResponseEntity.notFound().build();

        Invoice invoiceSimple = invoiceService.getInvoiceByID( idInvoice );
        if (!Objects.equals(invoiceSimple.getUser().getId(), ((UserDTO) Objects.requireNonNull(model.getAttribute("user"))).getId())) return ResponseEntity.notFound().build();

        UserDTO user = (UserDTO) model.getAttribute("user");
        assert user != null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // Let's get the hands dirty :]
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        //Metadata
        document.addTitle("Invoice-" + invoice.getDateBuy());
        document.addSubject("Invoice for " + user.getFirstName() + " " + user.getLastName1());
        document.addAuthor("ISP Hero");

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
        Font userAddress = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
        Font companyAddress = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);

        PdfPTable contact = new PdfPTable(2);
        contact.setWidthPercentage(100);

        PdfPCell companyCell = new PdfPCell();
        companyCell.setBorder(Rectangle.NO_BORDER);
        // Add company address
        Paragraph header = new Paragraph();
        header.setFont(companyAddress);
        header.setIndentationLeft(40);
        header.add("ISP Hero\n");
        header.add("1820 NW 56th St\n");
        header.add("Miami, Florida(FL),33142\n");
        header.add("United States\n");
        header.add("\n");
        companyCell.addElement(header);

        PdfPCell userCell = new PdfPCell();
        userCell.setBorder(Rectangle.NO_BORDER);
        // Add customer address
        Paragraph address = new Paragraph();
        address.setFont(userAddress);
        address.setIndentationLeft(10);
        address.add("Invoice to:\n");
        address.add(user.getFirstName() + " " + user.getLastName1() + " " + user.getLastName2() + "\n");
        address.add(user.getAddress() + "\n");
        address.add(user.getEmail() + "\n");
        address.add("\n");
        userCell.addElement(address);

        contact.addCell(userCell);
        contact.addCell(companyCell);

        document.add(contact);

        // Add some spacing between the contact info and the invoice details
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));

        // Add invoice details
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2f, 3f, 3f});

        PdfPCell cell = new PdfPCell();
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Quantity", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Price", font));
        table.addCell(cell);

        table.setHeaderRows(1);

        // Add invoice items
        invoice.getLines().forEach(line -> {
            cell.setPhrase(new Phrase(String.valueOf(line.getNameArticle()), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(String.valueOf(line.getQuantity()), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(line.getPrice() + " €", font));
            table.addCell(cell);
        });

        document.add(table);

        // Add total
        Paragraph total = new Paragraph();
        total.setFont(font);
        total.setAlignment(Element.ALIGN_RIGHT);
        total.add("Total: " + invoice.getTotal() + " €\n");
        document.add(total);

        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("Content-Disposition", "inline; filename=" + invoice.getFullName() + invoice.getDateBuy() + ".pdf");
        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
    }
}
