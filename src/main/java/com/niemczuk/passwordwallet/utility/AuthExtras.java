package com.niemczuk.passwordwallet.utility;

public class AuthExtras {

    private static String salt = null;

    public static String getSalt() {
        return salt;
    }

    public static void setSalt(String salt) {
        AuthExtras.salt = salt;
    }
}
