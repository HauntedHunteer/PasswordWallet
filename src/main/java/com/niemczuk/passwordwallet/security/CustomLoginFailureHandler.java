package com.niemczuk.passwordwallet.security;

import com.niemczuk.passwordwallet.dto.AppLoginSaveDto;
import com.niemczuk.passwordwallet.entity.User;
import com.niemczuk.passwordwallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;


@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String login = request.getParameter("login");
        User user = userService.findUserByLogin(login);

        String ipAddress = request.getRemoteAddr();
        LocalDateTime loginTime = LocalDateTime.now();

        AppLoginSaveDto loginData = new AppLoginSaveDto(user, loginTime, "fail", ipAddress);

        if (user !=null) {
            userService.registerAppLogin(loginData);
            if (user.isAccountNonLocked()) {
                if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                    userService.increaseFailedAttempts(user);
                } else {
                    userService.lock(user);
                    exception = new LockedException("Your account has been locked due to 3 failed attempts."
                            + " It will be unlocked after 10 minutes.");
                }
            } else {
                if (userService.unlockWhenTimeExpired(user)) {
                    exception = new LockedException("Your account has been unlocked. Try again.");
                }
            }
        }
        super.setDefaultFailureUrl("/signIn?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
