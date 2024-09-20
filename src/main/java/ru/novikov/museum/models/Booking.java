package ru.novikov.museum.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.novikov.museum.util.Phone;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_name")
    @NotEmpty(message = "Поле имя не должно быть пустым")
    @Size(min = 2, max = 30, message = "Размер поля имя должно быть между 2 и 30 символами")
    private String lastName;

    @Column(name = "first_name")
    @NotEmpty(message = "Поле фамилия не должно быть пустым")
    @Size(min = 2, max = 30, message = "Размер поля фамилия должно быть между 2 и 40 символами")
    private String firstName;

    @Column(name = "middle_name")
    @NotEmpty(message = "Поле отчество не должно быть пустым")
    @Size(min = 2, max = 30, message = "Размер поля отчество должно быть между 2 и 40 символами")
    private String middleName;

    @Column(name = "email")
    @Email
    @NotEmpty(message = "Поле email не должно быть пустым")
    private String email;

    @Column(name = "phone_number")
    @NotEmpty(message = "Поле номер телефона не должно быть пустым")
    @Phone(message = "Поле номер телефона может включать только цифры, скобки и тире")
    private String phoneNumber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
