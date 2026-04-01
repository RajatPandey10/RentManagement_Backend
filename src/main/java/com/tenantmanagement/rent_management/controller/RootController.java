package com.tenantmanagement.rent_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class RootController {

    @GetMapping
    public String checkHealth(){
        return "Application Health is good";
    }
}
