package com.azirariza.jflow.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping({"/api","/api/"})
public class HelloController {
    @GetMapping("ping")
    public String ping(){
        return "Hello From Java Backend!";
    }
}