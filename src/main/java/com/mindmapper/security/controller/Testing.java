package com.mindmapper.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Testing {

    @GetMapping("/")
    public String testing(){
        return "login successfully";
    }

}
