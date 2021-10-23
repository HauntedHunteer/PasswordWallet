package com.niemczuk.passwordwallet.service;

import com.niemczuk.passwordwallet.dto.ChangePasswordDto;
import com.niemczuk.passwordwallet.dto.PasswordPackageDto;
import com.niemczuk.passwordwallet.entity.Password;
import com.niemczuk.passwordwallet.entity.User;
import com.niemczuk.passwordwallet.repository.PasswordRepository;
import com.niemczuk.passwordwallet.repository.UserRepository;
import com.niemczuk.passwordwallet.security.passwordEncoder.HmacPasswordEncoder;
import com.niemczuk.passwordwallet.utility.AuthExtras;
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
public class PasswordService {

    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordService(UserRepository userRepository, PasswordRepository passwordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveNewWebsiteCredentials(PasswordPackageDto passwordPackageDto) throws Exception {
        User user = getLoggedUser();

        Key key = generateKey(user.getPasswordHash());
        String encryptedPasswordToStore = encrypt(passwordPackageDto.getPassword(), key);

        Password newPassword = Password.builder()
                .login(passwordPackageDto.getLogin())
                .password(encryptedPasswordToStore)
                .webAddress(passwordPackageDto.getWebAddress())
                .description(passwordPackageDto.getDescription())
                .user(user)
                .build();

        passwordRepository.save(newPassword);
    }

    public List<PasswordPackageDto> getPasswordList() throws Exception {
        User user = getLoggedUser();

        List<PasswordPackageDto> passwordList = passwordRepository.findAllByUserCustom(user);
        Key key = generateKey(user.getPasswordHash());

        passwordList.forEach(e -> {
            String password = e.getPassword();
            try {
                password = decrypt(password, key);
            }
            catch (Exception exception) {
                log.error("Decrypt fail", exception);
            }
            e.setPassword(password);
        });

        return passwordList;
    }

    public void changeUserPassword(ChangePasswordDto changePasswordDto) throws Exception {
        User user = getLoggedUser();
        String encodedPassword = null;

            if (!user.isPasswordKeptAsHash()) {
                encodedPassword = passwordEncoder.encode(AuthExtras.getSalt() + changePasswordDto.getNewPassword());
            }
            else {
                PasswordEncoder passwordEncoder = new HmacPasswordEncoder(AuthExtras.getKey());
                encodedPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());
            }

        List<Password> passwordList = passwordRepository.findAllByUser(user);

        Key oldPasswordKey = generateKey(user.getPasswordHash());
        Key newPasswordKey = generateKey(encodedPassword);

        decryptAndEncryptAllPasswords(passwordList, oldPasswordKey, newPasswordKey);

        user.setPasswordHash(encodedPassword);
        userRepository.save(user);
    }

    private void decryptAndEncryptAllPasswords(List<Password> passwordList, Key oldPasswordKey, Key newPasswordKey) {

        passwordList.forEach(e -> {
            String oldPassword = e.getPassword();
            String newPassword = "";
            try {
                oldPassword = decrypt(oldPassword, oldPasswordKey);
                newPassword = encrypt(oldPassword, newPasswordKey);

            } catch (Exception exception) {
                log.error("Decrypt list fail", exception);
                exception.printStackTrace();
            }
            e.setPassword(newPassword);
            passwordRepository.save(e);
        });
    }

    private User getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByLogin(auth.getName());
    }
}
