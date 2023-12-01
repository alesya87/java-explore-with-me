package ru.practicum.category.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryLogDto;
import ru.practicum.category.service.PublicCategoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
public class PublicCategoryController {
    private final PublicCategoryService publicCategoryService;

    public PublicCategoryController(PublicCategoryService publicCategoryService) {
        this.publicCategoryService = publicCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryLogDto>> get(@RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту: '/categories' на получение списка категорий");
        return new ResponseEntity<>(publicCategoryService.get(from, size), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryLogDto> get(@PathVariable Long catId) {
        log.info("Получен GET-запрос к эндпоинту: '/categories' на получение категории по id {}", catId);
        return new ResponseEntity<>(publicCategoryService.get(catId), HttpStatus.OK);
    }
}
