package ru.novikov.museum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.novikov.museum.models.Admin;
import ru.novikov.museum.models.Event;
import ru.novikov.museum.repositories.EventRepository;
import ru.novikov.museum.util.ResourceNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final AdminService adminService;

    @Autowired
    public EventService(EventRepository eventRepository, AdminService adminService) {
        this.eventRepository = eventRepository;
        this.adminService = adminService;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Мероприятие не найдено"));
    }

    @Transactional
    public void createEvent(Event event) {
        Admin currentAdmin = adminService.getCurrentAdmin();
        event.setCreatedWho(currentAdmin.getUsername());
        eventRepository.save(event);
    }

    @Transactional
    public void updateEvent(Long id, Event eventDetails) {
        Event event = getEventById(id);
        event.setTitleEvent(eventDetails.getTitleEvent());
        event.setStartDateTime(eventDetails.getStartDateTime());
        event.setEndDateTime(eventDetails.getEndDateTime());
        event.setMuseumName(eventDetails.getMuseumName());
        event.setAddress(eventDetails.getAddress());
        event.setFreeSeats(eventDetails.getFreeSeats());
        event.setTotalSeats(eventDetails.getTotalSeats());
        eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        eventRepository.delete(event);
    }
}
