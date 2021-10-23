package com.niemczuk.passwordwallet.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboardPage() {
        return "dashboard";
    }
}
