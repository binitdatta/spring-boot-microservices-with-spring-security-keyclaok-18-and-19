package com.rollingstone.spring.controller;

import com.rollingstone.response.UserDTO;
import com.rollingstone.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserRegistrationController {

    private static final String USER_REG_CONFIRMATION_PAGE ="regConfirmation";

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getRegistrationView(){
        return "user-registration";
    }

    @PostMapping("/register")
    public String userRegistration(final UserDTO user, final Model model){
        userService.save(user);
        return USER_REG_CONFIRMATION_PAGE;
    }
}
