package com.niemczuk.passwordwallet.security.passwordEncoder;

import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HmacPasswordEncoder implements PasswordEncoder {

    private static final String SSHA512_PREFIX = "{hmac-sha512}";
    private static final String HMAC_SHA512 = "HmacSHA512";

    private final String key;

    public HmacPasswordEncoder(String key) {
        this.key = key;
    }

    @Override
    public String encode(CharSequence text) {
        Mac sha512Hmac;
        String result = "";
        try {
            final byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            sha512Hmac = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(text.toString().getBytes(StandardCharsets.UTF_8));
            result = SSHA512_PREFIX + Base64.getEncoder().encodeToString(macData);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean matches(CharSequence plainTextPassword, String encodedPassword) {

        if (plainTextPassword == null || encodedPassword == null) {
            return false;
        }

        String encodedRawPassword = substractPrefixFromEncodedPassword(encode(plainTextPassword));

        return MessageDigest.isEqual(Utf8.encode(encodedRawPassword), Utf8.encode(encodedPassword));
    }

    private String substractPrefixFromEncodedPassword(String encodedPasswordWithPrefix) {
        return encodedPasswordWithPrefix.substring(13);
    }
}
