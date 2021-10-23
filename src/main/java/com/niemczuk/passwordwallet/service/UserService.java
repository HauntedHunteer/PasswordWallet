package com.niemczuk.passwordwallet.service;

import com.niemczuk.passwordwallet.dto.RegistrationDto;
import com.niemczuk.passwordwallet.entity.User;
import com.niemczuk.passwordwallet.repository.UserRepository;
import com.niemczuk.passwordwallet.security.passwordEncoder.HmacPasswordEncoder;
import com.niemczuk.passwordwallet.utility.AuthExtras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
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
}
