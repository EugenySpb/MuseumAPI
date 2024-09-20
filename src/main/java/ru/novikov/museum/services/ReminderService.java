package ru.novikov.museum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.novikov.museum.models.Booking;
import ru.novikov.museum.repositories.BookingRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {

    private final BookingRepository bookingRepository;
    private final EmailService emailService;
    private final Clock clock;

    @Autowired
    public ReminderService(BookingRepository bookingRepository, EmailService emailService, Clock clock) {
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
        this.clock = clock;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime reminderTime = now.plusDays(1);

        List<Booking> bookings = bookingRepository.findByEvent_StartDateTimeBetween(now, reminderTime);
        for (Booking booking : bookings) {
            emailService.sendReminderEmail(booking);
        }
    }
}
