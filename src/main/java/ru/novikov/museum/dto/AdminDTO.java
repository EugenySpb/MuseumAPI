package ru.novikov.museum.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
    @NotEmpty(message = "Имя пользователя не должно быть пустым")
    private String username;

    @NotEmpty(message = "Роль не должна быть пустой")
    private String role;
}
