package ru.novikov.museum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.novikov.museum.models.Booking;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    @Value("${server.api.base-url}")
    private String baseUrl;

    @Value("${server.api.from-email}")
    private String fromEmail;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(Booking booking) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(booking.getEmail());
            message.setFrom(fromEmail);
            message.setSubject("Подтверждение вашего бронирования");
            message.setText("Здравствуйте " + booking.getLastName() + ",\n\n" +
                    "Вы записались на музейное мероприятие. Подтверждаем, что ваша регистрация прошла успешно. Информация о мероприятии: \n" +
                    "Мероприятие: " + booking.getEvent().getTitleEvent() + "\n" +
                    "Дата и время: " + booking.getEvent().getStartDateTime() + "\n\n" +
                    "Место проведения: " + booking.getEvent().getAddress() + "\n\n" +
                    "Билеты оплачиваются в кассе музея перед началом мероприятия. \n" +
                    "Если вы хотите отменить бронирование, пожалуйста, перейдите по следующей ссылке: " +
                    baseUrl + "/api/bookings/" + booking.getId() + "/cancel\n\n" +
                    "С уважением,\n" +
                    "Команда музея");

            mailSender.send(message);
            logger.info("Письмо с подтверждением отправлено на " + booking.getEmail());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка отправки письма с подтверждением на адрес " + booking.getEmail(), e);
        }
    }

    public void sendReminderEmail(Booking booking) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(booking.getEmail());
            message.setFrom(fromEmail);
            message.setSubject("Напоминание о предстоящем мероприятии");
            message.setText("Здравствуйте " + booking.getLastName() + ",\n\n" +
                    "Напоминаем вам, что вы записаны на музейное мероприятие: \n" +
                    "Мероприятие: " + booking.getEvent().getTitleEvent() + "\n" +
                    "Дата и время: " + booking.getEvent().getStartDateTime() + "\n\n" +
                    "Место проведения: " + booking.getEvent().getAddress() + "\n\n" +
                    "Билеты оплачиваются в кассе музея перед началом мероприятия. \n" +
                    "Если вы хотите отменить бронирование, пожалуйста, перейдите по следующей ссылке: " +
                    baseUrl +"/api/bookings/" + booking.getId() + "/cancel\n\n" +
                    "С уважением,\n" +
                    "Команда музея");

            mailSender.send(message);
            logger.info("Письмо с напоминанием отправлено на адрес " + booking.getEmail());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка отправки электронного письма с напоминанием на " + booking.getEmail(), e);
        }
    }

    public void sendCancellationEmail(Booking booking) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(booking.getEmail());
            message.setFrom(fromEmail);
            message.setSubject("Отмена бронирования");
            message.setText("Здравствуйте " + booking.getLastName() + ",\n\n" +
                    "Ваше бронирование на мероприятие \"" + booking.getEvent().getTitleEvent() + "\" было отменено.\n\n" +
                    "С уважением,\n" +
                    "Команда музея");

            mailSender.send(message);
            logger.info("Письмо об отмене отправлено на адрес " + booking.getEmail());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка отправки письма об отмене на адрес " + booking.getEmail(), e);
        }
    }
}
