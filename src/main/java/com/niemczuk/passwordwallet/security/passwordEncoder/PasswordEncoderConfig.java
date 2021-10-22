package com.niemczuk.passwordwallet.security.passwordEncoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder delegatingPasswordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        //TODO encoders.put("hmac-sha512", new Hmac512PasswordEncoder(Constants.getKey()));
        encoders.put("sha512", new Sha512PasswordEncoder());
        return new DelegatingPasswordEncoder("sha512", encoders);
    }
}
