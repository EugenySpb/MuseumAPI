package ru.novikov.museum.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.novikov.museum.models.Booking;
import ru.novikov.museum.models.Event;
import ru.novikov.museum.repositories.BookingRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReminderServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReminderService reminderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Clock fixedClock = Clock.fixed(Instant.parse("2024-07-17T00:00:00Z"), ZoneId.systemDefault());
        reminderService = new ReminderService(bookingRepository, emailService, fixedClock);
    }

    @Test
    public void testReminder() {
        LocalDateTime now = LocalDateTime.now(Clock.fixed(Instant.parse("2024-07-17T00:00:00Z"), ZoneId.systemDefault()));
        LocalDateTime reminderTime = now.plusDays(1);

        Booking booking = new Booking();
        booking.setEmail("magadanzenit@gmail.com");
        booking.setFirstName("Иван");
        booking.setLastName("Иванов");

        Event event = new Event();
        event.setTitleEvent("Музейное мероприятие");
        event.setStartDateTime(reminderTime);
        event.setAddress("Адрес музея");

        booking.setEvent(event);

        when(bookingRepository.findByEvent_StartDateTimeBetween(now, reminderTime))
                .thenReturn(Collections.singletonList(booking));

        reminderService.sendReminders();

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(emailService, times(1)).sendReminderEmail(bookingCaptor.capture());

        Booking capturedBooking = bookingCaptor.getValue();
        assertEquals(booking.getEmail(), capturedBooking.getEmail());
        assertEquals(booking.getEvent().getTitleEvent(), capturedBooking.getEvent().getTitleEvent());
        assertEquals(booking.getEvent().getStartDateTime(), capturedBooking.getEvent().getStartDateTime());
    }
}
