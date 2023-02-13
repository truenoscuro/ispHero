package com.example.esquelet.controllers;

import com.example.esquelet.dtos.UserDTO;
import com.example.esquelet.entities.User;
import com.example.esquelet.services.TokenService;
import com.example.esquelet.services.UserService;

import com.example.esquelet.services.WaitingDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.exceptions.MailerSendException;

import java.util.Objects;


@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage","articleComplete"})
@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    TokenService tokenService;

    @Value("${MAIL_API_KEY}")
    private String MAIL_API_KEY;
    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("pageTitle","Register");
        return "backendUser/register";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute UserDTO user , Model model, @RequestParam(defaultValue = "false") String status) {
        if (Objects.equals(status, "false")) {
            String username = user.getUsername();
            // Check if any user has the same username || email
            if (userService.checkUser(username)) {
                model.addAttribute("error", "Username already exists");
                return "backendUser/register";
            }
            if (userService.checkEmail(user.getEmail())) {
                model.addAttribute("error", "Email already exists");
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
            if(model.containsAttribute("articleComplete")) return "redirect:/account/services/add";
            return "redirect:/account";
        } else {
            System.out.println("User or password incorrect");
            // Add error message
            model.addAttribute("case", "User or password incorrect");
            return "backendUser/login";
        }
    }


    //TODO use?-->
    @PostMapping("/message")
    public String messageSent(@RequestParam(name = "name") String name, @RequestParam(name = "email") String email, @RequestParam(name = "message") String message, Model model) {
        Email emailMessage = new Email();
        emailMessage.setFrom("Contact from ISP Hero", "info@isphero.com");
        emailMessage.addRecipient("ISP Hero", "info@isphero.com");
        emailMessage.setSubject("Contact from: " + name);
        emailMessage.setHtml(message);

        MailerSend mailerSend = new MailerSend();
        mailerSend.setToken(MAIL_API_KEY);

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

    @GetMapping("/validate/{token}")
    public String validate(@PathVariable String token, Model model) {
        if (tokenService.validateToken(token) == 0) {
            tokenService.getClaims(token);
            String email = tokenService.getClaims(token).get("email").toString();
            User user = userService.getUserByEmail(email);
            user.setVerified(true);
            userService.updateUser(user);
            model.addAttribute("status", "verified");
            return "backendUser/register";
        } else if (tokenService.validateToken(token) == 1) {
            System.out.println("Token expired");
            model.addAttribute("status", "expired");
            model.addAttribute("error", "Token expired. Please contact the administrator or register again.");
            return "backendUser/register";
        } else {
            System.out.println("Token invalid");
            model.addAttribute("status", "invalid");
            model.addAttribute("error", "Token invalid. Please contact the administrator or register again.");
            return "backendUser/register";
        }
    }
}
