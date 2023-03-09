package com.example.esquelet.controllers;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@Controller
@SessionAttributes(value = {"user","isLogged","cartUser","languages","langPage","urlCdn"})
public class HomeController {

    @GetMapping("/")
    public String index(Model model) { return "home/index"; }

    @GetMapping("/about")
    public String about( Model model ) {
        return "home/about";
    }

    @GetMapping("/contact")
    public String contact( Model model ) {
        return "home/contact";
    }

    @GetMapping("/terms-of-service")
    public String termsOfService( Model model ) {
        return "home/terms_of_service";
    }

    @GetMapping("/privacy-policy")
    public String privacyPolicy( Model model ) {
        return "home/privacy_policy";
    }

    @GetMapping("/cookies-policy")
    public String cookiesPolicy( Model model ) {
        return "home/cookies";
    }

    @RequestMapping(value = {"/robots", "/robot", "/robot.txt", "/robots.txt"})
    public void robot(HttpServletResponse response) {
        InputStream resourceAsStream = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            resourceAsStream = classLoader.getResourceAsStream("robots.txt");

            response.addHeader("Content-disposition", "filename=robots.txt");
            response.setContentType("text/plain");
            IOUtils.copy(resourceAsStream, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            System.out.println("Problem with displaying robot.txt" + e);
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (Exception e) {
                    System.out.println("Problem with displaying robot.txt" + e);
                }
            }
        }
    }

    @GetMapping(value = {"/sitemap.xml", "/sitemap"}, produces = "application/xml")
    public void getSitemap(HttpServletResponse response) throws IOException {
        InputStream resourceAsStream = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            resourceAsStream = classLoader.getResourceAsStream("sitemap.xml");

            response.addHeader("Content-disposition", "filename=sitemap.xml");
            response.setContentType("application/xml");
            IOUtils.copy(resourceAsStream, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            System.out.println("Problem with displaying sitemap.xml" + e);
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (Exception e) {
                    System.out.println("Problem with displaying sitemap.xml" + e);
                }
            }
        }
    }
}
