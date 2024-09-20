package ru.novikov.museum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.novikov.museum.models.Booking;
import ru.novikov.museum.models.Event;
import ru.novikov.museum.repositories.BookingRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookingService {
    private final BookingRepository bookingRepository;
    private final EventService eventService;
    private final EmailService emailService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, EventService eventService, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.eventService = eventService;
        this.emailService = emailService;
    }

    public List<Booking> getBookingsByEvent(Long eventId) {
        Event event = eventService.getEventById(eventId);
        return bookingRepository.findByEvent(event);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Transactional
    public void createBooking(Long eventId, Booking booking) {
        Event event = eventService.getEventById(eventId);
        if (event.getFreeSeats() <= 0) {
            throw new RuntimeException("Нет доступных мест для этого мероприятия");
        }

        booking.setEvent(event);
        Booking savedBooking = bookingRepository.save(booking);

        event.setFreeSeats(event.getFreeSeats() - 1);
        eventService.updateEvent(eventId, event);

        emailService.sendConfirmationEmail(savedBooking);

    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Бронь не найдена"));
        Event event = booking.getEvent();
        event.setFreeSeats(event.getFreeSeats() + 1);
        eventService.updateEvent(event.getId(), event);

        bookingRepository.delete(booking);
        emailService.sendCancellationEmail(booking);
    }
}
