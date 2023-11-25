package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    Set<Event> findByIdIn(Set<Long> ids);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:users) IS NULL OR e.initiator.id IN (:users)) " +
            "AND ((:states) IS NULL OR e.state IN (:states)) " +
            "AND ((:categories) IS NULL OR e.category.id IN (:categories)) " +
            "AND (cast(:rangeStart AS timestamp) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (cast(:rangeEnd AS timestamp) IS NULL OR e.eventDate <= :rangeEnd)")
    List<Event> getAllByAdmin(
            @Param("users") List<Long> users,
            @Param("states") List<EventState> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    @Query(value = "SELECT e FROM Event e " +
            "WHERE e.state = 'PUBLISHED' " +
            "AND (:text IS NULL OR lower(e.annotation) LIKE lower(concat('%',:text,'%')) " +
            "OR lower(e.description) LIKE lower(concat('%',:text,'%'))) " +
            "AND ((:categories) IS NULL OR e.category.id IN (:categories)) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (:onlyAvailable IS NOT FALSE OR e.confirmedRequests < e.participantLimit) " +
            "AND (cast(:rangeStart AS timestamp) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (cast(:rangeEnd AS timestamp) IS NULL OR e.eventDate <= :rangeEnd) ")
    List<Event> getAll(@Param("text") String text,
                       @Param("categories") List<Long> categories,
                       @Param("paid") Boolean paid,
                       @Param("rangeStart") LocalDateTime rangeStart,
                       @Param("rangeEnd") LocalDateTime rangeEnd,
                       @Param("onlyAvailable") Boolean onlyAvailable,
                       Pageable pageable);

    Optional<Event> findByIdAndState(Long id, EventState state);
}