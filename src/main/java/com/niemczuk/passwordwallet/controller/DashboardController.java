package com.niemczuk.passwordwallet.controller;

import com.niemczuk.passwordwallet.entity.User;
import com.niemczuk.passwordwallet.service.PasswordService;
import com.niemczuk.passwordwallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UserService userService;
    private final PasswordService passwordService;

    @Autowired
    public DashboardController(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }
    
    @GetMapping("/dashboard")
    public String showDashboardPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByLogin(auth.getName());
        model.addAttribute("user", user);
        return "dashboard";
    }
}
