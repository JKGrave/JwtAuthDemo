package com.example.demo.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ApiController {

    @RequestMapping("/")
    public String apiTest () {
        return "api Test Success ";
    }
}
