package com.niemczuk.passwordwallet.controller;

import com.niemczuk.passwordwallet.dto.RegistrationDto;
import com.niemczuk.passwordwallet.entity.User;
import com.niemczuk.passwordwallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signIn")
    public String showLoginPage() {
        return "signIn";
    }

    @GetMapping("/signUp")
    public String showRegistrationPage(Model model) {
        RegistrationDto registrationDto = new RegistrationDto();
        model.addAttribute("registrationDto", registrationDto);
        return "signUp";
    }

    @PostMapping("/signUp")
    public String register(@ModelAttribute RegistrationDto registrationDto) {
        // todo try with optional
        User possibleUser = userService.findUserByLogin(registrationDto.getLogin());

        if (possibleUser != null) {
            return "signUp";
        }
        else {
            userService.saveUser(registrationDto);
            return "signIn";
        }
    }
}
