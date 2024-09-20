package ru.novikov.museum.util;

public class EventNotCreatedException extends RuntimeException {
    public EventNotCreatedException(String msg) {
        super(msg);
    }
}
