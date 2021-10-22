package com.niemczuk.passwordwallet.security.passwordEncoder;

import com.niemczuk.passwordwallet.utility.AuthExtras;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha512PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence text) {
        try {
            //get an instance of SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            //calculate message digest of the input string  - returns byte array
            byte[] messageDigest = md.digest(text.toString().getBytes(StandardCharsets.UTF_8));

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            StringBuilder hashText = new StringBuilder(no.toString(16));

            // Add preceding 0s to make it 32 bit
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }

            // return the HashText
            return hashText.toString();
        }

        // If wrong message digest algorithm was specified
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean matches(CharSequence plainTextPassword, String encodedPassword) {
        String salt = AuthExtras.getSalt();

        if (plainTextPassword == null || encodedPassword == null) {
            return false;
        }

        String encodedRawPassword = encode(salt + plainTextPassword);

        return MessageDigest.isEqual(Utf8.encode(encodedRawPassword), Utf8.encode(encodedPassword));
    }
}
