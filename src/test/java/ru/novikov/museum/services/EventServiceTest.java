package ru.novikov.museum.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.novikov.museum.models.Admin;
import ru.novikov.museum.models.Event;
import ru.novikov.museum.repositories.EventRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private AdminService adminService;

    private Event createEvent(Long id, String title) {
        return new Event(id, title, LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Museum",
                "Address", 10, 100, LocalDateTime.now(), LocalDateTime.now(), "Admin");
    }

    @Test
    void getAllEvents() {
        List<Event> events = Arrays.asList(
                createEvent(1L, "Event 1"),
                createEvent(2L, "Event 2")
        );

        Mockito.when(eventRepository.findAll()).thenReturn(events);

        List<Event> foundEvents = eventService.getAllEvents();

        Assertions.assertEquals(2, foundEvents.size());
        Assertions.assertEquals("Event 1", foundEvents.get(0).getTitleEvent());
        Assertions.assertEquals("Event 2", foundEvents.get(1).getTitleEvent());

        Mockito.verify(eventRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getEventById() {
        Event event = createEvent(1L, "Title");

        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event foundEvent = eventService.getEventById(1L);
        Assertions.assertEquals("Title", foundEvent.getTitleEvent());
        Mockito.verify(eventRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void createEvent() {
        Event event = new Event();
        Admin admin = new Admin();
        admin.setUsername("adminUser");

        Mockito.when(adminService.getCurrentAdmin()).thenReturn(admin);

        eventService.createEvent(event);

        Mockito.verify(eventRepository, Mockito.times(1)).save(event);
        Assertions.assertEquals("adminUser", event.getCreatedWho());
    }

    @Test
    void updateEvent() {
        Event existingEvent = createEvent(1L, "Title");
        Event updatedEvent = new Event();
        updatedEvent.setTitleEvent("Updated Title");

        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(existingEvent));

        eventService.updateEvent(1L, updatedEvent);

        Assertions.assertEquals("Updated Title", existingEvent.getTitleEvent());
        Mockito.verify(eventRepository, Mockito.times(1)).save(existingEvent);
    }

    @Test
    void deleteEvent() {
        Event eventToDelete = createEvent(1L, "Title");

        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(eventToDelete));

        eventService.deleteEvent(1L);

        Mockito.verify(eventRepository, Mockito.times(1)).delete(eventToDelete);
    }
}