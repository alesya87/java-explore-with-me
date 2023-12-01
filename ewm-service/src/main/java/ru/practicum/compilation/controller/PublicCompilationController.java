package ru.practicum.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationLogDto;
import ru.practicum.compilation.service.PublicCompilationService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/compilations")
public class PublicCompilationController {
    private final PublicCompilationService publicCompilationService;

    public PublicCompilationController(PublicCompilationService publicCompilationService) {
        this.publicCompilationService = publicCompilationService;
    }

    @GetMapping
    public ResponseEntity<List<CompilationLogDto>> getAll(@RequestParam(required = false) Boolean pinned,
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту: '/compilations' на получение списка подборок");
        return new ResponseEntity<>(publicCompilationService.getAll(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationLogDto> getAll(@PathVariable Long compId) {
        log.info("Получен GET-запрос к эндпоинту: '/compilations/{compId}' на получение подборки по id={}", compId);
        return new ResponseEntity<>(publicCompilationService.getById(compId), HttpStatus.OK);
    }
}