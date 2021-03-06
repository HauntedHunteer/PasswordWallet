package com.niemczuk.passwordwallet.controller;

import com.niemczuk.passwordwallet.dto.AppLoginReadDto;
import com.niemczuk.passwordwallet.dto.ChangePasswordDto;
import com.niemczuk.passwordwallet.dto.PasswordPackageDto;
import com.niemczuk.passwordwallet.dto.SharePasswordPostDto;
import com.niemczuk.passwordwallet.entity.User;
import com.niemczuk.passwordwallet.service.PasswordService;
import com.niemczuk.passwordwallet.service.SharedPasswordService;
import com.niemczuk.passwordwallet.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
public class DashboardController {

    private final UserService userService;
    private final PasswordService passwordService;
    private final SharedPasswordService sharedPasswordService;

    @Autowired
    public DashboardController(UserService userService, PasswordService passwordService, SharedPasswordService sharedPasswordService) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.sharedPasswordService = sharedPasswordService;
    }

    @GetMapping("/dashboard")
    public String showDashboardPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByLogin(auth.getName());
        model.addAttribute("user", user);

        List<AppLoginReadDto> logins = userService.findAppLogins(user);
        model.addAttribute("logins", logins);
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
        SharePasswordPostDto postDto = new SharePasswordPostDto();
        model.addAttribute("postDto", postDto);
        model.addAttribute("passwordsList", passwordService.getPasswordList());
        return "websitesListPage";
    }

    @GetMapping("/changePassword")
    public String getChangePasswordFormPage(Model model) {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        model.addAttribute("changePasswordDto", changePasswordDto);
        return "changePasswordForm";
    }

    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute @Valid ChangePasswordDto changePasswordDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        passwordService.changeUserPassword(changePasswordDto);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @PostMapping("/sharePassword")
    public String sharePassword(@ModelAttribute SharePasswordPostDto sharePasswordPostDto) throws Exception {
        sharedPasswordService.sharePassword(sharePasswordPostDto);
        return "redirect:/dashboard";
    }

    @GetMapping("/sharedPasswordsLists")
    public String getSharedPasswordsLists(Model model) throws Exception {
        model.addAttribute("sharedPasswordListForOwner", sharedPasswordService.getSharedPasswordListForOwner());
        model.addAttribute("sharedPasswordListForUser", sharedPasswordService.getSharedPasswordListForUser());
        return "sharedPasswordListPage";
    }

    @GetMapping("/deleteSharedPassword/{id}")
    public String removeSharedPassword(@PathVariable("id") UUID sharedPasswordId) {
        sharedPasswordService.deleteSharedPassword(sharedPasswordId);
        return "redirect:/dashboard";
    }
}
