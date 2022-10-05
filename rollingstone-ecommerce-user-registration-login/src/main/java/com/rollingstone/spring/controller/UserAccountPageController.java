package com.rollingstone.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserAccountPageController {

    @GetMapping("/home")
    public String home(){
        return "home";
    }
}
