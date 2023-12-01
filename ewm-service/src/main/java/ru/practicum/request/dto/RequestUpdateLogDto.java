package ru.practicum.request.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateLogDto {
    private List<RequestLogDto> confirmedRequests;
    private List<RequestLogDto> rejectedRequests;
}