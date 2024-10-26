package ru.novikov.museum.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Base64;

public class PasswordHasher {
    public static void main(String[] args) {
        String originalString = "";
        String encodedString = Base64.getEncoder().encodeToString(originalString.getBytes());

        System.out.println("Закодированная строка: " + encodedString);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println(encodedPassword);
    }
}
