package ru.practicum.excception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.hibernate.exception.ConstraintViolationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler({MethodArgumentNotValidException.class, NumberFormatException.class,
            MissingServletRequestParameterException.class, BadRequestException.class,
            HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse("BAD_REQUEST", "Incorrectly made request.", e.getMessage(), LocalDateTime.now().format(DATE_TIME_FORMATTER));
    }

    @ExceptionHandler({ConstraintViolationException.class, javax.validation.ConstraintViolationException.class,
            DataIntegrityViolationException.class, ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse("CONFLICT", "Integrity constraint has been violated.", e.getMessage(), LocalDateTime.now().format(DATE_TIME_FORMATTER));
    }

    @ExceptionHandler({EmptyResultDataAccessException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse("NOT_FOUND", "The required object was not found.", e.getMessage(), LocalDateTime.now().format(DATE_TIME_FORMATTER));
    }

//    все-таки решила убрать, когда включаю, то он как будто периодически перехватывает обработку вышестоящих исключений
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleThrowable(final Exception e) {
//        log.error(e.getMessage());
//        return new ErrorResponse("INTERNAL_SERVER_ERROR", "Произошла непредвиденная ошибка.", "Произошла непредвиденная ошибка", LocalDateTime.now().format(DATE_TIME_FORMATTER));
//    }
}