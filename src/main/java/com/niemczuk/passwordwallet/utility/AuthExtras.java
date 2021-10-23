package com.niemczuk.passwordwallet.utility;

public class AuthExtras {

    private static String salt = null;
    private final static String key = "d313df8sd1h234566287da786643f0db";

    public static String getSalt() {
        return salt;
    }

    public static void setSalt(String salt) {
        AuthExtras.salt = salt;
    }

    public static String getKey() {
        return key;
    }
}
