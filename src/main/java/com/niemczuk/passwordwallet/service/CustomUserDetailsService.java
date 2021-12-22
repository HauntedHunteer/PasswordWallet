package com.niemczuk.passwordwallet.service;

import com.niemczuk.passwordwallet.entity.User;
import com.niemczuk.passwordwallet.repository.UserRepository;
import com.niemczuk.passwordwallet.security.CustomUserDetails;
import com.niemczuk.passwordwallet.utility.AuthExtras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(!userRepository.existsByLogin(username)) {
            throw new UsernameNotFoundException("Username not found");
        }

        User user = userRepository.findByLogin(username);
        AuthExtras.setSalt(user.getSalt());

        return new CustomUserDetails(user);
    }
}
