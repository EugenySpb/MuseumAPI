package ru.novikov.museum.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.novikov.museum.dto.EventDTO;
import ru.novikov.museum.dto.EventGetAllEventsDTO;
import ru.novikov.museum.models.Event;
import ru.novikov.museum.services.EventService;
import ru.novikov.museum.util.EventErrorResponse;
import ru.novikov.museum.util.EventNotCreatedException;
import ru.novikov.museum.util.EventValidator;

import java.util.List;
import java.util.stream.Collectors;

import static ru.novikov.museum.util.ErrorsUtil.returnErrorsEventToClient;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @Autowired
    public EventController(EventService eventService, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventService = eventService;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @GetMapping
    public List<EventGetAllEventsDTO> getAllEvents() {
        return eventService.getAllEvents().stream().map(this::convertToEventGetAllEventsDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable Long id) {
        return convertToEventDTO(eventService.getEventById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<String> createEvent(@RequestBody @Valid EventDTO eventDTO,
                                              BindingResult bindingResult) {
        Event eventToCreate = convertToEvent(eventDTO);

        eventValidator.validate(eventToCreate, bindingResult);

        if (bindingResult.hasErrors()) {
            returnErrorsEventToClient(bindingResult);
        }
        eventService.createEvent(eventToCreate);

        return ResponseEntity.ok("Мероприятие успешно создано");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEvent(@PathVariable Long id, @RequestBody @Valid EventDTO eventDetails, BindingResult bindingResult) {
        Event eventToCreate = convertToEvent(eventDetails);

        eventValidator.validate(eventToCreate, bindingResult);

        if (bindingResult.hasErrors()) {
            returnErrorsEventToClient(bindingResult);
        }
        eventService.updateEvent(id, eventToCreate);

        return ResponseEntity.ok("Мероприятие успешно обновлено");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Мероприятие удалено");
    }


    @ExceptionHandler
    private ResponseEntity<EventErrorResponse> handleException(EventNotCreatedException e) {
        EventErrorResponse response = new EventErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Event convertToEvent(EventDTO eventDTO) {
        return modelMapper.map(eventDTO, Event.class);
    }

    private EventDTO convertToEventDTO(Event event) {
        return modelMapper.map(event, EventDTO.class);
    }

    private EventGetAllEventsDTO convertToEventGetAllEventsDTO(Event event) {
        return modelMapper.map(event, EventGetAllEventsDTO.class);
    }
}
