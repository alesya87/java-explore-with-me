package ru.practicum.comment.dto;

import lombok.*;
import ru.practicum.comment.model.CommentStateAction;

import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentUpdateDto {
    @Size(min = 10)
    private String text;
    private CommentStateAction commentStateAction;
}
