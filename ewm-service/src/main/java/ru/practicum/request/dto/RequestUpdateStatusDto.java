package ru.practicum.request.dto;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.practicum.request.model.RequestStatus;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateStatusDto {
    private Set<Long> requestIds;
    private RequestStatus status;
}