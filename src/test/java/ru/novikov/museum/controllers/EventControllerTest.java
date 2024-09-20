package ru.novikov.museum.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.novikov.museum.dto.EventDTO;
import ru.novikov.museum.models.Event;
import ru.novikov.museum.services.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private EventDTO createEventDTO() {
        return new EventDTO("Title", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Museum", "Address", 10, 10);
    }

    @Test
    void getAllEvents() throws Exception {
        List<Event> events = List.of(new Event(1L, "Title", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Museum", "Address", 10, 100, LocalDateTime.now(), LocalDateTime.now(), "Admin"));

        Mockito.when(eventService.getAllEvents()).thenReturn(events);

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titleEvent").value("Title"))
                .andExpect(jsonPath("$[0].startDateTime").value(notNullValue()))
                .andExpect(jsonPath("$[0].endDateTime").value(notNullValue()))
                .andExpect(jsonPath("$[0].museumName").value("Museum"))
                .andExpect(jsonPath("$[0].address").value("Address"))
                .andExpect(jsonPath("$[0].freeSeats").value(10))
                .andExpect(jsonPath("$[0].totalSeats").value(100));
    }

    @Test
    void getEventById() throws Exception {
        Event event = new Event(1L, "Title", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Museum", "Address", 10, 100, LocalDateTime.now(), LocalDateTime.now(), "Admin");

        Mockito.when(eventService.getEventById(1L)).thenReturn(event);

        mockMvc.perform(get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titleEvent").value("Title"))
                .andExpect(jsonPath("$.startDateTime").value(notNullValue()))
                .andExpect(jsonPath("$.endDateTime").value(notNullValue()))
                .andExpect(jsonPath("$.museumName").value("Museum"))
                .andExpect(jsonPath("$.address").value("Address"))
                .andExpect(jsonPath("$.freeSeats").value(10))
                .andExpect(jsonPath("$.totalSeats").value(100));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createEvent() throws Exception {
        EventDTO eventDTO = createEventDTO();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Мероприятие успешно создано"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateEvent() throws Exception {
        EventDTO eventDTO = createEventDTO();

        mockMvc.perform(put("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Мероприятие успешно обновлено"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteEvent() throws Exception {
        mockMvc.perform(delete("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Мероприятие удалено"));
    }
}