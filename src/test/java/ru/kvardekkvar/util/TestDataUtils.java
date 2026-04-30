package ru.kvardekkvar.util;

import java.security.SecureRandom;

public final class TestDataUtils {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 32;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateToken() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return token.toString();
    }
}
