package ru.novikov.museum.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    @NotEmpty(message = "Поле название мероприятия не должно быть пустым")
    private String titleEvent;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @NotEmpty(message = "Поле название музея не должно быть пустым")
    private String museumName;

    @NotEmpty(message = "Поле адрес не должно быть пустым")
    private String address;

    private int freeSeats;

    @NotNull(message = "Поле общее количество мест не должно быть пустым")
    private int totalSeats;
}
