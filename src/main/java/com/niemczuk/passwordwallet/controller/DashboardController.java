package com.niemczuk.passwordwallet.controller;

import com.niemczuk.passwordwallet.dto.PasswordPackageDto;
import com.niemczuk.passwordwallet.entity.User;
import com.niemczuk.passwordwallet.service.PasswordService;
import com.niemczuk.passwordwallet.service.UserService;
import com.niemczuk.passwordwallet.utility.AuthExtras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



import javax.validation.Valid;

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

    @GetMapping("/website")
    public String getNewWebsiteFormPage(Model model) {
        PasswordPackageDto passwordPackageDto = new PasswordPackageDto();
        model.addAttribute("passwordPackageDto", passwordPackageDto);
        return "newWebsiteForm";
    }

    @PostMapping("/website")
    public String createNewWebsiteCredentials(@ModelAttribute @Valid PasswordPackageDto passwordPackageDto) throws Exception {
        passwordService.saveNewWebsiteCredentials(passwordPackageDto);
        return "redirect:/dashboard";
    }

    @GetMapping("/websitesList")
    public String getPasswordList(Model model) throws Exception {
        model.addAttribute("passwordsList", passwordService.getPasswordList());
        return "websitesListPage";
    }
}
