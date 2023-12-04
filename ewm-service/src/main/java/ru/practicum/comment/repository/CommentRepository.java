package ru.practicum.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentStatus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndAuthorId(Long commentId, Long userId);

    List<Comment> findByAuthorIdOrderByCreatedOnDesc(Long userId, Pageable pageable);

    List<Comment> findByEventIdAndStatusOrderByCreatedOnDesc(Long eventId, CommentStatus status, Pageable pageable);

    @Query("SELECT c FROM Comment c " +
            "WHERE ((:users) IS NULL OR c.author.id IN (:users)) " +
            "AND (:text IS NULL OR lower(c.text) LIKE lower(concat('%',:text,'%'))) " +
            "AND ((:statuses) IS NULL OR c.status IN (:statuses)) " +
            "AND ((:events) IS NULL OR c.event.id IN (:events)) " +
            "AND (cast(:rangeStart AS timestamp) IS NULL OR c.createdOn >= :rangeStart) " +
            "AND (cast(:rangeEnd AS timestamp) IS NULL OR c.createdOn <= :rangeEnd) " +
            "ORDER BY c.createdOn DESC")
    List<Comment> getAllByAdmin(
            @Param("users") List<Long> users,
            @Param("text") String text,
            @Param("statuses") List<CommentStatus> statuses,
            @Param("events") List<Long> events,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);
}
