package com.example.edugo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerRedirectController {

    @GetMapping({"/", "", "/docs", "/swagger"})
    public String redirectToSwagger() {
        // Avec context-path=/api, le chemin réel est /swagger-ui/index.html
        return "redirect:/swagger-ui/index.html";
    }
}


