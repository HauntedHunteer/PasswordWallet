package com.niemczuk.passwordwallet.service;

import com.niemczuk.passwordwallet.dto.SharePasswordPostDto;
import com.niemczuk.passwordwallet.dto.SharedPasswordForUserDto;
import com.niemczuk.passwordwallet.dto.SharedPasswordOwnerDto;
import com.niemczuk.passwordwallet.entity.Password;
import com.niemczuk.passwordwallet.entity.SharedPassword;
import com.niemczuk.passwordwallet.entity.User;
import com.niemczuk.passwordwallet.repository.PasswordRepository;
import com.niemczuk.passwordwallet.repository.SharedPasswordRepository;
import com.niemczuk.passwordwallet.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.niemczuk.passwordwallet.utility.AESenc.*;

import java.security.Key;
import java.util.List;

@Slf4j
@Service
public class SharedPasswordService {
    private final UserRepository userRepository;
    private final SharedPasswordRepository sharedPasswordRepository;
    private final PasswordRepository passwordRepository;

    @Autowired
    public SharedPasswordService(UserRepository userRepository, SharedPasswordRepository sharedPasswordRepository, PasswordRepository passwordRepository) {
        this.userRepository = userRepository;
        this.sharedPasswordRepository = sharedPasswordRepository;
        this.passwordRepository = passwordRepository;
    }

    public void sharePassword(SharePasswordPostDto postDto) throws Exception {
        User owner = getLoggedUser();
        User userForShare = userRepository.findByLogin(postDto.getUsername());
        Password ownerPassword = passwordRepository.getById(postDto.getPasswordId());

        // decrypt owner password
        Key ownerKey = generateKey(owner.getPasswordHash());
        String planeTextPassword = "";
        try {
            planeTextPassword = decrypt(ownerPassword.getPassword(), ownerKey);
        } catch (Exception exception) {
            log.error("Decrypt fail", exception);
        }

        // encrypt for share user
        Key shareUserKey = generateKey(userForShare.getPasswordHash());
        String encryptedPasswordForNewUser = encrypt(planeTextPassword, shareUserKey);

        // save to db
        SharedPassword newSharedPassword = SharedPassword.builder()
                .sharedPassword(encryptedPasswordForNewUser)
                .ownerPassword(ownerPassword.getPassword())
                .webAddress(ownerPassword.getWebAddress())
                .description(ownerPassword.getDescription())
                .login(ownerPassword.getLogin())
                .owner(owner)
                .sharedTo(userForShare)
                .build();
        sharedPasswordRepository.save(newSharedPassword);
    }

    public List<SharedPasswordOwnerDto> getSharedPasswordListForOwner() throws Exception {
        User user = getLoggedUser();

        List<SharedPasswordOwnerDto> passwordListForOwner =
                sharedPasswordRepository.findSharedPasswordByOwner(user);
        Key key = generateKey(user.getPasswordHash());

        passwordListForOwner.forEach(e -> {
            String password = e.getPassword();
            try {
                password = decrypt(password, key);
            }
            catch (Exception exception) {
                log.error("Decrypt fail", exception);
            }
            e.setPassword(password);
        });

        return passwordListForOwner;
    }

    public List<SharedPasswordForUserDto> getSharedPasswordListForUser() throws Exception {
        User user = getLoggedUser();

        List<SharedPasswordForUserDto> passwordListForUser =
                sharedPasswordRepository.findSharedPasswordBySharedTo(user);
        Key key = generateKey(user.getPasswordHash());

        passwordListForUser.forEach(e -> {
            String password = e.getPassword();
            try {
                password = decrypt(password, key);
            }
            catch (Exception exception) {
                log.error("Decrypt fail", exception);
            }
            e.setPassword(password);
        });

        return passwordListForUser;
    }

    private User getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByLogin(auth.getName());
    }
}
