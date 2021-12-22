package com.niemczuk.passwordwallet.service;

import com.niemczuk.passwordwallet.dto.AppLoginDto;
import com.niemczuk.passwordwallet.dto.RegistrationDto;
import com.niemczuk.passwordwallet.entity.AppLogin;
import com.niemczuk.passwordwallet.entity.User;
import com.niemczuk.passwordwallet.repository.AppLoginRepository;
import com.niemczuk.passwordwallet.repository.UserRepository;
import com.niemczuk.passwordwallet.security.passwordEncoder.HmacPasswordEncoder;
import com.niemczuk.passwordwallet.utility.AuthExtras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class UserService {

    public static final int MAX_FAILED_ATTEMPTS = 3;

    private static final long LOCK_TIME_DURATION_MIN = 10;

    private final UserRepository userRepository;
    private final AppLoginRepository appLoginRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, AppLoginRepository appLoginRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.appLoginRepository = appLoginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserByLogin(String login) {
        if (userRepository.existsByLogin(login)) {
            return userRepository.findByLogin(login);
        }

        return null;
    }

    public void saveUser(RegistrationDto registrationDto) {
        String encodedPassword;
        String salt = null;

        if(!registrationDto.isPasswordKeptAsHash()) {
            salt = generateSalt();
            encodedPassword = passwordEncoder.encode(salt + registrationDto.getPassword());
        }
        else {
            PasswordEncoder passwordEncoder = new HmacPasswordEncoder(AuthExtras.getKey());
            encodedPassword = passwordEncoder.encode(registrationDto.getPassword());
        }

        User newUser = User.builder()
                .login(registrationDto.getLogin())
                .passwordHash(encodedPassword)
                .salt(salt)
                .isPasswordKeptAsHash(registrationDto.isPasswordKeptAsHash())
                .accountNonLocked(true)
                .build();

        userRepository.save(newUser);
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[14];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }

    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;

        userRepository.updateFailedAttempts(newFailAttempts, user.getLogin());
    }

    public void resetFailedAttempts(String login) {
        userRepository.updateFailedAttempts(0, login);
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(LocalDateTime.now());

        userRepository.save(user);
    }

    public boolean unlockWhenTimeExpired(User user) {
        LocalDateTime lockTime = user.getLockTime();
        LocalDateTime unlockTime = lockTime.plusMinutes(LOCK_TIME_DURATION_MIN);

        if (unlockTime.isBefore(LocalDateTime.now())) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);

            userRepository.save(user);

            return true;
        }

        return false;
    }

    public void registerAppLogin(AppLoginDto appLoginDto) {
        AppLogin appLogin = AppLogin.builder()
                .user(appLoginDto.getUser())
                .loginTime(appLoginDto.getLoginTime())
                .loginResult(appLoginDto.getLoginResult())
                .ipAddress(appLoginDto.getIpAddress())
                .build();
        appLoginRepository.save(appLogin);
    }
}
