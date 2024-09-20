package ru.novikov.museum.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.novikov.museum.util.Phone;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingGetDTO {
    @NotEmpty(message = "Поле имя не должно быть пустым")
    @Size(min = 2, max = 30, message = "Размер поля имя должно быть между 2 и 30 символами")
    private String lastName;

    @NotEmpty(message = "Поле фамилия не должно быть пустым")
    @Size(min = 2, max = 30, message = "Размер поля фамилия должно быть между 2 и 40 символами")
    private String firstName;

    @NotEmpty(message = "Поле отчество не должно быть пустым")
    @Size(min = 2, max = 30, message = "Размер поля отчество должно быть между 2 и 40 символами")
    private String middleName;

    @Email
    @NotEmpty(message = "Поле email не должно быть пустым")
    private String email;

    @NotEmpty(message = "Поле номер телефона не должно быть пустым")
    @Phone(message = "Поле номер телефона может включать только цифры, скобки и тире")
    private String phoneNumber;

    private EventForGetBookingDTO event;

}
