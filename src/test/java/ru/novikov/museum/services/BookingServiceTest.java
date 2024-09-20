package ru.novikov.museum.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.novikov.museum.models.Booking;
import ru.novikov.museum.models.Event;
import ru.novikov.museum.repositories.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private EventService eventService;

    @MockBean
    private EmailService emailService;

    private Event createEvent(Long id, int freeSeats) {
        Event event = new Event();
        event.setId(id);
        event.setFreeSeats(freeSeats);
        return event;
    }

    private Booking createBooking(Long id, Event event) {
        return new Booking(id, "Иван", "Иванов", "Иванович", "test@test.com",
                "1234567890", LocalDateTime.now(), event);
    }

    @Test
    void getBookingsByEvent() {
        Event event = createEvent(1L, 10);
        List<Booking> bookings = List.of(createBooking(1L, event));

        Mockito.when(eventService.getEventById(1L)).thenReturn(event);
        Mockito.when(bookingRepository.findByEvent(event)).thenReturn(bookings);

        List<Booking> foundBookings = bookingService.getBookingsByEvent(1L);
        Assertions.assertEquals(1, foundBookings.size());
        Mockito.verify(eventService, Mockito.times(1)).getEventById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1)).findByEvent(event);
    }

    @Test
    void getAllBookings() {
        List<Booking> bookings = List.of(createBooking(1L, new Event()), createBooking(2L, new Event()));

        Mockito.when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> foundBookings = bookingService.getAllBookings();
        Assertions.assertEquals(2, foundBookings.size());
        Mockito.verify(bookingRepository, Mockito.times(1)).findAll();
    }

    @Test
    void createBooking() {
        Event event = createEvent(1L, 10);
        Booking booking = createBooking(null, event);

        Mockito.when(eventService.getEventById(1L)).thenReturn(event);
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(1L);
            return b;
        });

        bookingService.createBooking(1L, booking);

        Mockito.verify(bookingRepository, Mockito.times(1)).save(booking);
        Mockito.verify(emailService, Mockito.times(1)).sendConfirmationEmail(Mockito.any(Booking.class));
        Assertions.assertEquals(9, event.getFreeSeats());
    }


    @Test
    void cancelBooking() {
        Event event = createEvent(1L, 9);
        Booking booking = createBooking(1L, event);

        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        Mockito.doNothing().when(bookingRepository).delete(booking);

        bookingService.cancelBooking(1L);

        Assertions.assertEquals(10, event.getFreeSeats());
        Mockito.verify(bookingRepository, Mockito.times(1)).delete(booking);
        Mockito.verify(emailService, Mockito.times(1)).sendCancellationEmail(booking);
        Mockito.verify(eventService, Mockito.times(1)).updateEvent(event.getId(), event);
    }
}