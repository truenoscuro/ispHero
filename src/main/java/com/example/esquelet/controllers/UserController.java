package com.example.esquelet.controllers;

import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.services.UserService;

import com.example.esquelet.services.WaitingDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.exceptions.MailerSendException;

import java.util.Objects;


@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage"})

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WaitingDomainService waitingDomainService;

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("pageTitle","Register");
        return "backendUser/register";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute UserDTO user , Model model, @RequestParam(defaultValue = "false") String status) {
        if (Objects.equals(status, "false")) {
            String username = user.getUsername();
            // Check if any user has the same username
            if (userService.checkUser(username)) {
                model.addAttribute("error", "Username already exists");
                return "backendUser/register";
            }
            user.setRole("USER");
            user.setVerified(false);
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userService.addUser(user);
            model.addAttribute("user",userService.getUser(user));
            model.addAttribute("isLogged",true);
            model.addAttribute("status", "true");

            userService.sendRegisterMail(user);
        }
        else {
            model.addAttribute("status", "false");
        }
        return "backendUser/register";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user",new UserDTO() );
        model.addAttribute("pageTitle", "Login");
        return "backendUser/login";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("user",new UserDTO());
        model.addAttribute("isLogged",false);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO user, Model model) {
        // Get user and password from form
        String userName = user.getUsername();
        String password = user.getPassword();
        // Check user and password
        if (userService.checkUser(userName, password)) {
            model.addAttribute("isLogged",true);
            model.addAttribute("user",userService.getUser( user ) );
            model.addAttribute("userName", userName);
            model.addAttribute("isLogged", true);
            return "redirect:/account";
        } else {
            System.out.println("User or password incorrect");
            // Add error message
            model.addAttribute("case", "User or password incorrect");
            return "backendUser/login";
        }
    }

    @PostMapping("/message")
    public String messageSent(@RequestParam(name = "name") String name, @RequestParam(name = "email") String email, @RequestParam(name = "message") String message, Model model) {
        Email emailMessage = new Email();
        emailMessage.setFrom("Contact from ISP Hero", "info@isphero.com");
        emailMessage.addRecipient("ISP Hero", "info@isphero.com");
        emailMessage.setSubject("Contact from: " + name);
        emailMessage.setHtml(message);

        MailerSend mailerSend = new MailerSend();
        mailerSend.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiOGZhMzlmZTdlMmVkZTM0ODJkZDYyMDRkNzQxMmE1MDJlMmM4YWFlNWJkNmZjNmY0ZWQzY2E4NTY3YTFhYTk2ZmEyOGNjY2FiNTNkYjdjOWUiLCJpYXQiOjE2NzUyNDQ0MTguOTIzMSwibmJmIjoxNjc1MjQ0NDE4LjkyMzEwMywiZXhwIjo0ODMwOTE4MDE4LjkxNjc2Nywic3ViIjoiNTYwOTIiLCJzY29wZXMiOlsiZW1haWxfZnVsbCIsImRvbWFpbnNfZnVsbCIsImFjdGl2aXR5X2Z1bGwiLCJhbmFseXRpY3NfZnVsbCIsInRva2Vuc19mdWxsIiwid2ViaG9va3NfZnVsbCIsInRlbXBsYXRlc19mdWxsIiwic3VwcHJlc3Npb25zX2Z1bGwiLCJzbXNfZnVsbCIsImVtYWlsX3ZlcmlmaWNhdGlvbl9mdWxsIiwiaW5ib3VuZHNfZnVsbCIsInJlY2lwaWVudHNfZnVsbCIsInNlbmRlcl9pZGVudGl0eV9mdWxsIl19.fFXonujENY0bK7HoMPrTHWnH-eWBXwuTePS_DFp75ay010aus_zlony_FXhnB01KGx4XSI-GCEG9iJjuzIaPFivOMv62htYniRsZmKFyDFGLSGJmriC4Fzsl4K9MFO9TF_0m69DocCAGlwkE0SD3NWJS0VyQjqJh1oYZLdif4BzMQQYOy41WFMPSqu665IN120C0ZJU4dZdCW78nhbiM3D-lJl2sdDITflqiSyHcMscmg0R-sr8YD_vZsZ0ju81nH24i7Zx2iT7BtDHkfN9aduB7kO0YlTofT_zFGY6FntHs1Ub1axy6Tlg0Bg_UpoPL3Baf8qIuMlsXnvmPleRP3GKPNUJdRd-SkT4JD8voEtEO6eoNf6Xbz5R6_aSlkIFdNfXYDWfVRR-KzOtuAtQAZZ7JxzC_a1wSAFm9cYndl1C83ZkSrONio8m-uab3cgqmpZ-51ZtxgEfxoJyAMZeoxkNQPjONvGNXgDsmR2ktGeDrTzXgwUpUvEn9tHQkWuwUTzgQRBKGt1rkDiNhzQ9l0D4f0ZVROY6lCEP4yMQq6KWKBf32H3VovE9O3TFBJY9OYx17tnUykWCw81NYx0Z8sx68gZhE_GzBjZKi42aOifcy3k11JhKIF_svcl4dxQDvaUcqUblwlWpcZXSYM-hnyeSlShPqZUQS2W11_zDbw4I");

        try {
            MailerSendResponse response = mailerSend.emails().send(emailMessage);
            System.out.println(response.messageId);
        } catch (MailerSendException e) {
            e.printStackTrace();
        }

        return "backendUser/messagesent";
    }

    @GetMapping("/password-recovery")
    public String passwordRecovery(Model model) {
        return "backendUser/password-recovery";
    }

    @PostMapping("/password-recovery")
    public String passwordRecovery(@ModelAttribute UserDTO user, Model model) {
        if (userService.checkUser(user.getUsername())) {
            // TODO: userService.sendPasswordRecoveryMail(user);
            model.addAttribute("status", "true");
        } else {
            model.addAttribute("status", "false");
        }
        return "backendUser/messagesent";
    }

}
