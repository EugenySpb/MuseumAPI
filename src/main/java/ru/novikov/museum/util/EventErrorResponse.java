package ru.novikov.museum.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventErrorResponse {
    private String message;
    private long timestamp;
}
