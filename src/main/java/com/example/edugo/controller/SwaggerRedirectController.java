package com.example.edugo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerRedirectController {

    @GetMapping({"/", "", "/docs", "/swagger"})
    public String redirectToSwagger() {
        return "redirect:/api/swagger-ui/index.html";
    }
}


