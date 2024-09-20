package ru.novikov.museum.util;

public class AdminNotCreatedException extends RuntimeException {
    public AdminNotCreatedException(String message) {
        super(message);
    }
}
