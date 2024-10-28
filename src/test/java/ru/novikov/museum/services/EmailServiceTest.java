package ru.novikov.museum.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.novikov.museum.models.Booking;
import ru.novikov.museum.models.Event;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;
    @Value("${server.api.base-url}")
    private String baseUrl;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testSendConfirmationEmail() {
        Booking booking = new Booking();
        booking.setEmail("test@gmail.com");
        booking.setFirstName("Иван");
        booking.setLastName("Иванов");

        Event event = new Event();
        event.setTitleEvent("Музейное мероприятие");
        event.setStartDateTime(LocalDateTime.now().plusDays(1));
        event.setAddress("Адрес музея");

        booking.setEvent(event);

        emailService.sendConfirmationEmail(booking);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("test@gmail.com", Objects.requireNonNull(sentMessage.getTo())[0]);
        assertEquals(fromEmail, sentMessage.getFrom());
        assertEquals("Подтверждение вашего бронирования", sentMessage.getSubject());
        assertEquals("Здравствуйте Иванов,\n\n" +
                "Вы записались на музейное мероприятие. Подтверждаем, что ваша регистрация прошла успешно. Информация о мероприятии: \n" +
                "Мероприятие: Музейное мероприятие\n" +
                "Дата и время: " + event.getStartDateTime() + "\n\n" +
                "Место проведения: Адрес музея\n\n" +
                "Билеты оплачиваются в кассе музея перед началом мероприятия. \n" +
                "Если вы хотите отменить бронирование, пожалуйста, перейдите по следующей ссылке: " +
                baseUrl + "/api/bookings/" + booking.getId() + "/cancel\n\n" +
                "С уважением,\n" +
                "Команда музея", sentMessage.getText());
    }
}