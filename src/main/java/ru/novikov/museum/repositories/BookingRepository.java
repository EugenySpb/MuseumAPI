package ru.novikov.museum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.novikov.museum.models.Booking;
import ru.novikov.museum.models.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByEvent(Event event);

    List<Booking> findByEvent_StartDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
