package ru.practicum.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationAddDto;
import ru.practicum.compilation.dto.CompilationLogDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    public AdminCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public ResponseEntity<CompilationLogDto> save(@Valid @RequestBody CompilationAddDto compilationAddDto) {
        log.info("Получен POST-запрос к эндпоинту: '/admin/compilations' на добавление подборки: {}", compilationAddDto);
        return new ResponseEntity<>(compilationService.save(compilationAddDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        log.info("Получен DELETE-запрос к эндпоинту: '/admin/compilations/{compId}' на удаление подборки по id={}", compId);
        compilationService.delete(compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationLogDto> update(@Valid @RequestBody CompilationUpdateDto compilationUpdateDto,
                                                    @PathVariable Long compId) {
        log.info("Получен PATCH-запрос к эндпоинту: '/admin/compilations/{compId}' на изменение подборки с id={}: {}",
                compId, compilationUpdateDto);
        return new ResponseEntity<>(compilationService.update(compilationUpdateDto, compId), HttpStatus.OK);
    }
}