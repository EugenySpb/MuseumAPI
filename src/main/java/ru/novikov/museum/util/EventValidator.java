package ru.novikov.museum.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.novikov.museum.models.Event;

@Component
public class EventValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Event.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Event event = (Event) target;

        if (event.getStartDateTime() == null) {
            errors.rejectValue("startDateTime", "NotNull", "Дата начала мероприятия не должна быть пустой");
        }

        if (event.getEndDateTime() == null) {
            errors.rejectValue("endDateTime", "NotNull", "Дата окончания мероприятия не должна быть пустой");
        }

        // Проверка, что дата начала мероприятия раньше даты окончания
        if (event.getStartDateTime() != null && event.getEndDateTime() != null) {
            if (event.getStartDateTime().isAfter(event.getEndDateTime())) {
                errors.rejectValue("startDateTime", "StartDateTimeAfterEndDateTime",
                        "Дата начала мероприятия должна быть раньше даты окончания");
            }
        }

        // Проверка, что количество свободных мест не превышает общее количество мест
        if (event.getFreeSeats() > event.getTotalSeats()) {
            errors.rejectValue("freeSeats", "FreeSeatsExceedsTotalSeats",
                    "Количество свободных мест не может превышать общее количество мест");
        }

    }
}
