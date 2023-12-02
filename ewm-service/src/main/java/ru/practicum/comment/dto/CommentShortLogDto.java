package ru.practicum.comment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CommentShortLogDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private LocalDateTime updatedOn;
}
