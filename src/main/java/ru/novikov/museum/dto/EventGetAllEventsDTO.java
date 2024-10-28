package ru.novikov.museum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventGetAllEventsDTO {
    private Long id;
    private String titleEvent;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String museumName;
    private String address;
    private int freeSeats;
    private int totalSeats;
}
