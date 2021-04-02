package com.vaibsD.springdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

@Controller
public class AppSecurityController {
    @GetMapping("/login")
    public String showLoginPage() {
        Logger logger = Logger.getLogger("AppSecurityController.class");
        logger.info("In controller");
        return "login";
    }

    @GetMapping("/noAccess")
    public String showAccessDeniedPage() {
        return "access-denied";
    }
}
