package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitAddDto;
import ru.practicum.HitLogDto;
import ru.practicum.StatsLogDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    public ResponseEntity<HitLogDto> save(@RequestBody HitAddDto hitAddDto) {
        log.info("Получен запрос POST /hit hit={}", hitAddDto);
        return new ResponseEntity<>(statsService.save(hitAddDto), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatsLogDto>> get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                         @RequestParam(required = false) List<String> uris,
                                         @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Получен запрос GET /stats с параметрами: start {}, end {}, uris {}, unique {}", start, end, uris, unique);
        return new ResponseEntity<>(statsService.get(start, end, uris, unique), HttpStatus.OK);
    }
}
