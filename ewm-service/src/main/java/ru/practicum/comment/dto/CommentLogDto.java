package ru.practicum.comment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.practicum.comment.model.CommentStatus;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CommentLogDto {
    private Long id;
    private String text;
    private EventShortDto event;
    private UserShortDto author;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private LocalDateTime updatedOn;
    private CommentStatus status;
}
