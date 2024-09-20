package ru.novikov.museum.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.novikov.museum.dto.BookingDTO;
import ru.novikov.museum.dto.BookingGetDTO;
import ru.novikov.museum.models.Booking;
import ru.novikov.museum.services.BookingService;
import ru.novikov.museum.util.BookingErrorResponse;
import ru.novikov.museum.util.BookingNotCreatedException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.novikov.museum.util.ErrorsUtil.returnErrorsBookingToClient;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ModelMapper modelMapper;

    @Autowired
    public BookingController(BookingService bookingService, ModelMapper modelMapper) {
        this.bookingService = bookingService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<BookingGetDTO> getAllBookings() {
        return bookingService.getAllBookings().stream().map(this::convertToBookingGetDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{eventId}")
    public List<BookingGetDTO> getBookingsByEvent(@PathVariable Long eventId) {
        return bookingService.getBookingsByEvent(eventId).stream().map(this::convertToBookingGetDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<String> createBooking(@PathVariable Long eventId, @RequestBody @Valid BookingDTO bookingDTO,
                                                BindingResult bindingResult) {
        Booking bookingToCreate = convertToBooking(bookingDTO);

        if (bindingResult.hasErrors()) {
            returnErrorsBookingToClient(bindingResult);
        }
        bookingService.createBooking(eventId, bookingToCreate);

        return ResponseEntity.ok("Запись успешно осуществлена");
    }

    @DeleteMapping("/{bookingId}/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Бронирование отменено");
    }

    @ExceptionHandler
    private ResponseEntity<BookingErrorResponse> handleException(BookingNotCreatedException e) {
        BookingErrorResponse response = new BookingErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Booking convertToBooking(BookingDTO bookingDTO) {
        return modelMapper.map(bookingDTO, Booking.class);
    }

    private BookingGetDTO convertToBookingGetDTO(Booking booking) {
        return modelMapper.map(booking, BookingGetDTO.class);
    }
}
