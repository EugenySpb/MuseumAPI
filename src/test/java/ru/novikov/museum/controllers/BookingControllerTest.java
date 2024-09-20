package ru.novikov.museum.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.novikov.museum.dto.BookingDTO;
import ru.novikov.museum.models.Booking;
import ru.novikov.museum.models.Event;
import ru.novikov.museum.services.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllBookings() throws Exception {
        List<Booking> bookings = List.of(
                new Booking(1L, "lastName", "firstName", "middleName", "test@test.com", "1234567890", LocalDateTime.now(), new Event())
        );

        Mockito.when(bookingService.getAllBookings()).thenReturn(bookings);

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("lastName"))
                .andExpect(jsonPath("$[0].firstName").value("firstName"))
                .andExpect(jsonPath("$[0].middleName").value("middleName"))
                .andExpect(jsonPath("$[0].email").value("test@test.com"))
                .andExpect(jsonPath("$[0].phoneNumber").value("1234567890"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getBookingsByEvent() throws Exception {
        List<Booking> bookings = List.of(
                new Booking(1L, "lastName", "firstName", "middleName", "email@example.com", "1234567890", LocalDateTime.now(), new Event())
        );

        Mockito.when(bookingService.getBookingsByEvent(1L)).thenReturn(bookings);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("lastName"))
                .andExpect(jsonPath("$[0].firstName").value("firstName"))
                .andExpect(jsonPath("$[0].middleName").value("middleName"))
                .andExpect(jsonPath("$[0].email").value("email@example.com"))
                .andExpect(jsonPath("$[0].phoneNumber").value("1234567890"));
    }

    @Test
    void createBooking() throws Exception {
        BookingDTO bookingDTO = new BookingDTO("lastName", "firstName", "middleName", "email@example.com", "1234567890");

        mockMvc.perform(post("/api/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookingDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Запись успешно осуществлена"));
    }

    @Test
    void cancelBooking() throws Exception {
        mockMvc.perform(delete("/api/bookings/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().string("Бронирование отменено"));
    }
}